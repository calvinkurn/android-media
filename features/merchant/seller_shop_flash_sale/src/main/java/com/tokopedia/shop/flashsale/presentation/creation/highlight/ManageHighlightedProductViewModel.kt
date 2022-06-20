package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageHighlightedProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PAGE_SIZE = 10
        private const val PRODUCT_LIST_TYPE_ID = 0
    }

    private val _products = MutableLiveData<Result<List<HighlightableProduct>>>()
    val products: LiveData<Result<List<HighlightableProduct>>>
        get() = _products

    private var shouldDisableProductSelection = false
    private var selectedProductIds: MutableList<String> = mutableListOf()

    fun getProducts(
        campaignId: Long,
        productName: String,
        offset: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    productName = productName,
                    listType = PRODUCT_LIST_TYPE_ID,
                    pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, offset)
                )
                val products = campaigns.productList.map {
                    HighlightableProduct(
                        it.productId,
                        it.productName,
                        it.imageUrl.img200,
                        it.productMapData.originalPrice.toString(),
                        it.productMapData.discountedPrice.toString(),
                        it.productMapData.discountPercentage.toString(),
                        disabled = false,
                        isSelected = false
                    )
                }
                val sortedProducts = sort(products)
                _products.postValue(Success(sortedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    private fun sort(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products.sortedBy { it.selectedAtMillis }
    }

    fun setDisableProductSelection(shouldDisableProductSelection: Boolean) {
        this.shouldDisableProductSelection = shouldDisableProductSelection
    }

    fun shouldDisableProductSelection(): Boolean {
        return shouldDisableProductSelection
    }

    fun getSelectedProductIds(): List<String> {
        return selectedProductIds
    }

    fun addProductToSelection(product: HighlightableProduct) {
        this.selectedProductIds.add(product.id)
    }

    fun removeProductFromSelection(product: HighlightableProduct) {
        this.selectedProductIds.remove(product.id)
    }
}