package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.HighlightedProduct
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignHighlightProductsUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageHighlightedProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
    private val getSellerCampaignHighlightProductsUseCase: GetSellerCampaignHighlightProductsUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PAGE_SIZE = 10
        private const val PRODUCT_LIST_TYPE_ID = 0
        private const val MAX_PRODUCT_SELECTION = 5
    }

    private val _products = MutableLiveData<Result<List<HighlightableProduct>>>()
    val products: LiveData<Result<List<HighlightableProduct>>>
        get() = _products

    private val _highlightedProducts = MutableLiveData<Result<List<HighlightedProduct>>>()
    val highlightedProducts: LiveData<Result<List<HighlightedProduct>>>
        get() = _highlightedProducts

    private var selectedProductIds: MutableList<String> = mutableListOf()

    fun getProducts(
        campaignId: Long,
        productName: String,
        disableProductsOnNextPage: Boolean,
        offset: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val products = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    productName = productName,
                    listType = PRODUCT_LIST_TYPE_ID,
                    pagination = GetSellerCampaignProductListRequest.Pagination(PAGE_SIZE, offset)
                )
                val updatedProducts = handleProductEnabledState(disableProductsOnNextPage, products.productList)
                _products.postValue(Success(updatedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    fun getHighlightedProducts(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val products = getSellerCampaignHighlightProductsUseCase.execute(campaignId)
                _highlightedProducts.postValue(Success(products))
            },
            onError = { error ->
                _highlightedProducts.postValue(Fail(error))
            }
        )
    }

    private fun handleProductEnabledState(
        disableProductsOnNextPage: Boolean,
        products: List<SellerCampaignProductList.Product>
    ): List<HighlightableProduct> {
        return products.map {
            val isSelected =
                it.highlightProductWording.isNotEmpty() || it.productId in selectedProductIds

            HighlightableProduct(
                it.productId,
                it.productName,
                it.imageUrl.img200,
                it.productMapData.originalPrice,
                it.productMapData.discountedPrice,
                it.productMapData.discountPercentage,
                disabled = disableProductsOnNextPage,
                isSelected = isSelected
            )
        }
    }

    fun getSelectedProductIds(): List<String> {
        return selectedProductIds
    }

    fun addProductToSelection(productId: String) {
        this.selectedProductIds.add(productId)
    }

    fun removeProductFromSelection(productId : String) {
        this.selectedProductIds.remove(productId)
    }

    fun markAsSelected(
        selectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return products
            .map { product ->
                if (selectedProduct.id == product.id) {
                    product.copy(isSelected = true)
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
                    product.copy(isSelected = false)
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