package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorMessage
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase.Companion.ACTION_DELETE
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ManageProductMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ROWS = 50
        private const val OFFSET = 0
        private const val MIN_CAMPAIGN_STOCK = 1
        private const val MIN_CAMPAIGN_DISCOUNTED_PRICE = 100
        private const val MAX_CAMPAIGN_DISCOUNT_PERCENTAGE = 0.99
    }

    private val _products = MutableLiveData<Result<SellerCampaignProductList>>()
    val products: LiveData<Result<SellerCampaignProductList>>
        get() = _products

    private val _bannerType = MutableLiveData(HIDE.type)
    val bannerType: LiveData<Int>
        get() = _bannerType

    private val _removeProductsStatus = MutableLiveData<Result<Boolean>>()
    val removeProductsStatus: LiveData<Result<Boolean>>
        get() = _removeProductsStatus

    fun getProducts(
        campaignId: Long,
        listType: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    listType = listType,
                    pagination = GetSellerCampaignProductListRequest.Pagination(
                        ROWS,
                        OFFSET
                    )
                )
                _products.postValue(Success(campaigns))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    fun setProductErrorMessage(productList: SellerCampaignProductList) {
        productList.productList.forEach { product ->
            product.errorMessage = getProductErrorMessage(product.productMapData)
        }
    }

    fun setProductInfoCompletion(productList: SellerCampaignProductList) {
        productList.productList.forEach { product ->
            product.isInfoComplete = isProductInfoComplete(product.productMapData)
        }
    }

    fun getBannerType(productList: SellerCampaignProductList) {
        productList.productList.forEach { product ->
            when {
                getProductErrorMessage(product.productMapData).isNotEmpty() -> {
                    _bannerType.postValue(ERROR.type)
                }
                else -> {
                    when {
                        !isProductInfoComplete(product.productMapData) -> {
                            if (bannerType.value != ERROR.type) {
                                _bannerType.postValue(EMPTY.type)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isProductInfoComplete(productMapData: SellerCampaignProductList.ProductMapData): Boolean {
        return when {
            productMapData.discountedPrice.isZero() -> false
            productMapData.discountPercentage.isZero() -> false
            productMapData.originalCustomStock.isZero() -> false
            productMapData.customStock.isZero() -> false
            productMapData.maxOrder.isZero() -> false
            else -> true
        }
    }

    private fun getProductErrorMessage(productMapData: SellerCampaignProductList.ProductMapData): String {
        var errorMsg = ""
        val maxDiscountedPrice =
            (productMapData.originalPrice * MAX_CAMPAIGN_DISCOUNT_PERCENTAGE).toInt()
                .convertRupiah()
        when {
            productMapData.discountedPrice > productMapData.originalPrice -> {
                errorMsg =
                    ManageProductErrorMessage.MAX_CAMPAIGN_DISCOUNTED_PRICE.errorMsg + maxDiscountedPrice
                when {
                    productMapData.customStock > productMapData.originalStock -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.discountedPrice < MIN_CAMPAIGN_DISCOUNTED_PRICE -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.customStock < MIN_CAMPAIGN_STOCK -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.maxOrder > productMapData.customStock -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                }
            }

            productMapData.customStock > productMapData.originalStock -> {
                errorMsg =
                    ManageProductErrorMessage.MAX_CAMPAIGN_STOCK.errorMsg + "${productMapData.originalStock}"
                when {
                    productMapData.discountedPrice < MIN_CAMPAIGN_DISCOUNTED_PRICE -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.customStock < MIN_CAMPAIGN_STOCK -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.maxOrder > productMapData.customStock -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                }
            }

            productMapData.discountedPrice < MIN_CAMPAIGN_DISCOUNTED_PRICE -> {
                errorMsg = ManageProductErrorMessage.MIN_CAMPAIGN_DISCOUNTED_PRICE.errorMsg
                when {
                    productMapData.customStock < MIN_CAMPAIGN_STOCK -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                    productMapData.maxOrder > productMapData.customStock -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                }
            }

            productMapData.customStock < MIN_CAMPAIGN_STOCK -> {
                errorMsg = ManageProductErrorMessage.MIN_CAMPAIGN_STOCK.errorMsg
                when {
                    productMapData.maxOrder > productMapData.customStock -> {
                        errorMsg += ManageProductErrorMessage.OTHER.errorMsg
                    }
                }
            }

            productMapData.maxOrder > productMapData.customStock -> {
                errorMsg += ManageProductErrorMessage.MAX_CAMPAIGN_ORDER.errorMsg
            }
        }

        return errorMsg
    }

    fun removeProducts(
        campaignId: Long,
        productList: List<SellerCampaignProductList.Product>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId = campaignId.toString(),
                    productData = ManageProductMapper.mapToProductDataList(productList),
                    action = ACTION_DELETE
                )
                _removeProductsStatus.postValue(Success(campaigns))
            },
            onError = { error ->
                _removeProductsStatus.postValue(Fail(error))
            }
        )
    }

}