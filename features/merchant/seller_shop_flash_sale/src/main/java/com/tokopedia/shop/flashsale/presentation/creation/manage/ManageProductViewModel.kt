package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
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
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase,
    private val productErrorStatusHandler: ProductErrorStatusHandler
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ROWS = 50
        private const val OFFSET = 0
    }

    private val _products = MutableLiveData<Result<SellerCampaignProductList>>()
    val products: LiveData<Result<SellerCampaignProductList>>
        get() = _products

    private val _bannerType = MutableLiveData(HIDE_BANNER)
    val bannerType: LiveData<ManageProductBannerType>
        get() = _bannerType

    private val _removeProductsStatus = MutableLiveData<Result<Boolean>>()
    val removeProductsStatus: LiveData<Result<Boolean>>
        get() = _removeProductsStatus

    val incompleteProducts = Transformations.map(products) {
        ManageProductMapper.filterInfoNotCompleted(it)
    }

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
            val productErrorType = productErrorStatusHandler.getErrorType(product.productMapData)
            product.errorMessage = productErrorStatusHandler.getProductListErrorMessage(
                productErrorType,
                product.productMapData
            )
        }
    }

    fun setProductInfoCompletion(productList: SellerCampaignProductList) {
        productList.productList.forEach { product ->
            product.isInfoComplete = isProductInfoComplete(product.productMapData)
        }
    }

    fun getBannerType(productList: SellerCampaignProductList) {
        productList.productList.forEach { product ->
            if (productErrorStatusHandler.getErrorType(product.productMapData) != NOT_ERROR) {
                _bannerType.postValue(ERROR_BANNER)
            } else {
                if (!isProductInfoComplete(product.productMapData)) {
                    if (bannerType.value != ERROR_BANNER) {
                        _bannerType.postValue(EMPTY_BANNER)
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
                    action = ProductionSubmissionAction.DELETE
                )
                _removeProductsStatus.postValue(Success(campaigns.isSuccess))
            },
            onError = { error ->
                _removeProductsStatus.postValue(Fail(error))
            }
        )
    }
}