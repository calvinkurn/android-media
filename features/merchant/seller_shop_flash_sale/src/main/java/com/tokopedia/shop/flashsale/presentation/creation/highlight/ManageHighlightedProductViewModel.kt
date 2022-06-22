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
import java.util.*
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
                val disabled = selectedProductIds.size >= 5
                val products = campaigns.productList.map {
                    HighlightableProduct(
                        it.productId,
                        it.productName,
                        it.imageUrl.img200,
                        it.productMapData.originalPrice,
                        it.productMapData.discountedPrice,
                        it.productMapData.discountPercentage,
                        disabled = disabled,
                        isSelected = it.highlightProductWording.isNotEmpty(),
                        selectedAtMillis = Date().time
                    )
                }
                val sortedProducts = sortProductsBySelectionTime(products)
                _products.postValue(Success(sortedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    private fun sortProductsBySelectionTime(products: List<HighlightableProduct>): List<HighlightableProduct> {
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

    fun markAsSelected(
        selectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return products
            .map { product ->
                if (selectedProduct.id == product.id) {
                    product.copy(isSelected = true, selectedAtMillis = Date().time, disabled = false)
                } else {
                    product
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun markAsUnselected(
        selectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return products
            .map { product ->
                if (selectedProduct.id == product.id) {
                    product.copy(isSelected = false, selectedAtMillis = 0)
                } else {
                    product
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun disableAllUnselectedProducts(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .map { product ->
                if (product.isSelected) {
                    product
                } else {
                    product.copy(disabled = true)
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun enableAllUnselectedProducts(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .map { product ->
                if (product.isSelected) {
                    product
                } else {
                    product.copy(disabled = false)
                }
            }
            .sortedByDescending { it.isSelected }
    }
}