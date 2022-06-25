package com.tokopedia.shop.flashsale.presentation.creation.highlight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.data.mapper.HighlightableProductRequestMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ManageHighlightedProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase,
    private val mapper: HighlightableProductRequestMapper
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PRODUCT_LIST_TYPE_ID = 0
        private const val OFFSET_BY_ONE = 1
        private const val MAX_PRODUCT_SELECTION = 5
    }

    private val _products = MutableLiveData<Result<List<HighlightableProduct>>>()
    val products: LiveData<Result<List<HighlightableProduct>>>
        get() = _products

    private val _submit = MutableLiveData<Result<ProductSubmissionResult>>()
    val submit: LiveData<Result<ProductSubmissionResult>>
        get() = _submit

    private var selectedProductIds: MutableSet<Long> = mutableSetOf()

    private var isFirstLoad = true

    fun setIsFirstLoad(isFirstLoad : Boolean) {
        this.isFirstLoad = isFirstLoad
    }

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
                //val isFirstLoad = offset == 0 && xinSearchMode
                val updatedProducts = handleProductEnabledState(products.productList, isFirstLoad)
                val sortedProducts = sortByHighlightedProducts(updatedProducts)
                _products.postValue(Success(sortedProducts))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    fun submitHighlightedProducts(campaignId: Long, products: List<HighlightableProduct>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val mappedProducts = mapper.map(products)
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId.toString(),
                    ProductionSubmissionAction.SUBMIT,
                    mappedProducts
                )
                _submit.postValue(Success(result))
            },
            onError = { error ->
                _submit.postValue(Fail(error))
            }
        )
    }

    private fun handleProductEnabledState(
        products: List<SellerCampaignProductList.Product>,
        isFirstLoad: Boolean
    ): List<HighlightableProduct> {
        return products.mapIndexed { index, product ->

            //first time, is first page = true,  isSelected = product.highlightProductWording.isNotEmpty()
            //search (load from network), is first page = false, isSelected = product.productId.toLongOrZero() in selectedProductIds
            //clear, is first page = false, isSelected = product.productId.toLongOrZero() in selectedProductIds


            val isSelected = if (isFirstLoad) {
                product.highlightProductWording.isNotEmpty()
            } else {
                product.productId.toLongOrZero() in selectedProductIds
            }

            val disabled = selectedProductIds.size == MAX_PRODUCT_SELECTION && !isSelected
            HighlightableProduct(
                product.productId.toLongOrZero(),
                product.productName,
                product.imageUrl.img200,
                product.productMapData.originalPrice,
                product.productMapData.discountedPrice,
                product.productMapData.discountPercentage,
                product.productMapData.customStock,
                product.warehouseList.map { HighlightableProduct.Warehouse(it.warehouseId.toLongOrZero(), it.customStock.toLong()) },
                product.productMapData.maxOrder,
                disabled,
                isSelected,
                index + OFFSET_BY_ONE
            )
        }
    }

    private fun sortByHighlightedProducts(products: List<HighlightableProduct>): List<HighlightableProduct> {
        return products.sortedByDescending { it.isSelected }
    }

    fun addProductIdToSelection(productId: Long) {
        this.selectedProductIds.add(productId)
    }


    fun getSelectedProductIds(): Set<Long> {
        return this.selectedProductIds
    }

    fun removeProductIdFromSelection(productId : Long) {
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