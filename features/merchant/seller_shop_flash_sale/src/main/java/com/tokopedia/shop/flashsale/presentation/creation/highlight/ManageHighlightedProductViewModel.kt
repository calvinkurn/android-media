package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
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
        private const val PRODUCT_LIST_TYPE_ID = 0
        private const val OFFSET_BY_ONE = 1
        private const val MAX_PRODUCT_SELECTION = 5
    }

    private val _products = MutableLiveData<Result<List<HighlightableProduct>>>()
    val products: LiveData<Result<List<HighlightableProduct>>>
        get() = _products

    private var selectedProductIds: MutableList<String> = mutableListOf()

    fun getProducts(
        campaignId: Long,
        productName: String,
        pageSize : Int,
        offset: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val products = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    productName = productName,
                    listType = PRODUCT_LIST_TYPE_ID,
                    pagination = GetSellerCampaignProductListRequest.Pagination(pageSize, offset)
                )
                val updatedProducts = handleProductEnabledState(products.productList)
                _products.postValue(Success(updatedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    private fun handleProductEnabledState(
        products: List<SellerCampaignProductList.Product>
    ): List<HighlightableProduct> {
        val totalSelected = products.filter { it.highlightProductWording.isNotEmpty() }.size
        return products.mapIndexed { index, product ->
            val isSelected = product.highlightProductWording.isNotEmpty() || product.productId in selectedProductIds
            val disabled = totalSelected == MAX_PRODUCT_SELECTION && !isSelected
            HighlightableProduct(
                product.productId,
                product.productName,
                product.imageUrl.img200,
                product.productMapData.originalPrice,
                product.productMapData.discountedPrice,
                product.productMapData.discountPercentage,
                disabled,
                isSelected,
                index + OFFSET_BY_ONE
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
            .mapIndexed { index, product  ->
                if (selectedProduct.id == product.id) {
                    product.copy(isSelected = true, position = index + OFFSET_BY_ONE)
                } else {
                    product.copy(position = index + OFFSET_BY_ONE)
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun markAsUnselected(
        selectedProduct: HighlightableProduct,
        products: List<HighlightableProduct>
    ): List<HighlightableProduct> {
        return products
            .mapIndexed { index, product  ->
                if (selectedProduct.id == product.id) {
                    product.copy(isSelected = false, position = index + OFFSET_BY_ONE)
                } else {
                    product.copy(position = index + OFFSET_BY_ONE)
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun disableAllUnselectedProducts(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .mapIndexed { index, product  ->
                if (product.isSelected) {
                    product.copy(position = index + OFFSET_BY_ONE)
                } else {
                    product.copy(disabled = true, position = index + OFFSET_BY_ONE)
                }
            }
            .sortedByDescending { it.isSelected }
    }

    fun enableAllUnselectedProducts(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products
            .mapIndexed { index, product  ->
                if (product.isSelected) {
                    product.copy(position = index + OFFSET_BY_ONE)
                } else {
                    product.copy(disabled = false, position = index + OFFSET_BY_ONE)
                }
            }
            .sortedByDescending { it.isSelected }
    }
}