package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorMessage
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.ManageProductListAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase
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
                    GetSellerCampaignProductListRequest.Pagination(
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

    fun getProductErrorMessage(productMapData: SellerCampaignProductList.ProductMapData): String {
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

}