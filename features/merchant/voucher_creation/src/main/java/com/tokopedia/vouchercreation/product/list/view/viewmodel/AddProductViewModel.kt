package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortInput
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListMetaResponse.Category
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListMetaResponse.Sort
import com.tokopedia.vouchercreation.product.list.domain.usecase.*
import com.tokopedia.vouchercreation.product.list.view.model.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getProductListUseCase: GetProductListUseCase,
        private val getProductVariantsUseCase: GetProductVariantsUseCase,
        private val getWarehouseLocationsUseCase: GetWarehouseLocationsUseCase,
        private val getShowCasesByIdUseCase: GetShowCasesByIdUseCase,
        private val getProductListMetaDataUseCase: GetProductListMetaDataUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        const val SELLER_LOCATION_ID = 340735
    }

    // SORT AND FILTER PROPERTIES
    private var searchKeyWord: String? = null
    private var warehouseLocationId: Int? = null
    private var showCaseSelections = listOf<ShowCaseSelection>()
    private var categorySelections = listOf<CategorySelection>()
    private var selectedSort: GoodsSortInput? = null

    // PRODUCT SELECTIONS
    private var selectedProducts: MutableList<ProductUiModel> = mutableListOf()

    // ADAPTER
    private var adapterPosition: Int? = null

    // LIVE DATA
    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val productListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData
    private val getProductVariantsResultLiveData = MutableLiveData<Result<GetProductV3Response>>()
    val getProductVariantsResult: LiveData<Result<GetProductV3Response>> get() = getProductVariantsResultLiveData
    private val getWarehouseLocationsResultLiveData = MutableLiveData<Result<ShopLocGetWarehouseByShopIdsResponse>>()
    val getSellerLocationsResult: LiveData<Result<ShopLocGetWarehouseByShopIdsResponse>> get() = getWarehouseLocationsResultLiveData
    private val getShowCasesByIdResultLiveData = MutableLiveData<Result<ShopShowcasesByShopIdResponse>>()
    val getShowCasesByIdResult: LiveData<Result<ShopShowcasesByShopIdResponse>> get() = getShowCasesByIdResultLiveData
    private val getProductListMetaDataResultLiveData = MutableLiveData<Result<ProductListMetaResponse>>()
    val getProductListMetaDataResult: LiveData<Result<ProductListMetaResponse>> get() = getProductListMetaDataResultLiveData

    fun getProductList(
            keyword: String? = null,
            shopId: String? = null,
            warehouseLocationId: Int? = null,
            shopShowCaseIds: List<String> = listOf(),
            categoryList: List<String> = listOf(),
            sort: GoodsSortInput? = null
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListUseCase.createRequestParams(
                        keyword = keyword,
                        shopId = shopId,
                        warehouseId = warehouseLocationId.toString(),
                        shopShowCaseIds = shopShowCaseIds,
                        categories = categoryList,
                        sort = sort
                )
                getProductListUseCase.setRequestParams(params = params.parameters)
                getProductListUseCase.executeOnBackground()
            }
            getProductListResultLiveData.value = Success(result)
        }, onError = {
            getProductListResultLiveData.value = Fail(it)
        })
    }

    fun getProductVariants(isVariantEmpty: Boolean, productId: String) {
        if (!isVariantEmpty) return
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductVariantsUseCase.createRequestParams(productId)
                getProductVariantsUseCase.setRequestParams(params = params.parameters)
                getProductVariantsUseCase.executeOnBackground()
            }
            getProductVariantsResultLiveData.value = Success(result)
        }, onError = {
            getProductVariantsResultLiveData.value = Fail(it)
        })
    }

    fun getWarehouseLocations(shopId: Int) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetWarehouseLocationsUseCase.createRequestParams(shopId)
                getWarehouseLocationsUseCase.setRequestParams(params = params.parameters)
                getWarehouseLocationsUseCase.executeOnBackground()
            }
            getWarehouseLocationsResultLiveData.value = Success(result)
        }, onError = {
            getWarehouseLocationsResultLiveData.value = Fail(it)
        })
    }

    fun getProductListMetaData(shopId: String, warehouseLocationId: Int) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListMetaDataUseCase.createParams(shopId, warehouseLocationId.toString())
                getProductListMetaDataUseCase.setRequestParams(params = params.parameters)
                getProductListMetaDataUseCase.executeOnBackground()
            }
            getProductListMetaDataResultLiveData.value = Success(result)
        }, onError = {
            getProductListMetaDataResultLiveData.value = Fail(it)
        })
    }

    fun getShopShowCases(shopId: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetShowCasesByIdUseCase.createParams(shopId)
                getShowCasesByIdUseCase.setRequestParams(params = params.parameters)
                getShowCasesByIdUseCase.executeOnBackground()
            }
            getShowCasesByIdResultLiveData.value = Success(result)
        }, onError = {
            getShowCasesByIdResultLiveData.value = Fail(it)
        })
    }

    fun mapProductDataToProductUiModel(productDataList: List<ProductData>): List<ProductUiModel> {
        return productDataList.map { productData ->
            // TODO: implement proper string formatting
            ProductUiModel(
                    imageUrl = productData.pictures.first().urlThumbnail,
                    id = productData.id,
                    productName = productData.name,
                    sku = "SKU : " + productData.sku,
                    price = "Rp " + productData.price.max.toString(),
                    soldNStock = "Terjual " + productData.stock.toString() + " | " + "Stok " + productData.txStats.sold,
                    hasVariant = productData.isVariant
            )
        }
    }

    fun mapVariantDataToVariantUiModel(variantData: Variant, productUiModel: ProductUiModel): List<VariantUiModel> {
        return variantData.products.map { productVariant ->
            VariantUiModel(
                    variantId = productVariant.id,
                    variantName = getVariantName(productVariant.combination, variantData.selections),
                    sku = productUiModel.sku,
                    price = productUiModel.price,
                    soldNStock = productUiModel.soldNStock
            )
        }
    }

    fun mapWarehouseLocationToSelections(warehouses: List<Warehouses>): List<WarehouseLocationSelection> {
        return warehouses.map { warehouse ->
            WarehouseLocationSelection(
                    warehouseId = warehouse.warehouseId,
                    warehouseName = warehouse.warehouseName,
                    isSelected = false
            )
        }
    }

    fun mapShopShowCasesToSelections(shopShowcases: List<ShopShowcase>): List<ShowCaseSelection> {
        return shopShowcases.map { shopShowcase ->
            ShowCaseSelection(
                    id = shopShowcase.id,
                    name = shopShowcase.name
            )
        }
    }

    fun mapSortListToSortSelections(sortList: List<Sort>): List<SortSelection> {
        return sortList.map { sort ->
            SortSelection(
                    id = sort.id,
                    value = sort.value,
                    name = sort.name,
            )
        }
    }

    fun mapCategoriesToCategorySelections(categoryList: List<Category>): List<CategorySelection> {
        return categoryList.map { category ->
            CategorySelection(
                    id = category.id,
                    value = category.value,
                    name = category.name
            )
        }
    }

    private fun getVariantName(combination: List<Int>, selections: List<Selection>): String {
        val sb = StringBuilder()
        combination.forEach { index ->
            sb.append(selections[index].options[index].value)
            sb.append(" ")
        }
        return sb.toString().trim()
    }

    // SORT AND FILTER
    fun setSearchKeyword(keyword: String) {
        this.searchKeyWord = keyword
    }

    fun getSearchKeyWord(): String? {
        return searchKeyWord
    }

    fun setWarehouseLocationId(warehouseLocation: Int) {
        this.warehouseLocationId = warehouseLocation
    }

    fun getWarehouseLocationId(): Int? {
        return warehouseLocationId
    }

    fun setSelectedShowCases(showCaseSelections: List<ShowCaseSelection>) {
        this.showCaseSelections = showCaseSelections
    }

    fun getSelectedShopShowCaseIds(): List<String> {
        return showCaseSelections.map { showCaseSelection ->
            showCaseSelection.id
        }
    }

    fun setSelectedCategories(categorySelections: List<CategorySelection>) {
        this.categorySelections = categorySelections
    }

    fun getSelectedCategoryIds(): List<String> {
        return categorySelections.map { categorySelection ->
            categorySelection.id
        }
    }

    fun setSelectedSort(sortSelections: List<SortSelection>) {
        sortSelections.firstOrNull()?.let {
            this.selectedSort = GoodsSortInput(it.id, it.value)
        }
    }

    fun getSelectedSort(): GoodsSortInput? {
        return selectedSort
    }

    // PRODUCT SELECTIONS
    fun addSelectedProduct(product: ProductUiModel) {
        selectedProducts.add(product)
    }

    fun removeSelectedProduct(product: ProductUiModel) {
        selectedProducts.removeFirst { it.id == product.id }
    }

    fun setSelectedProduct(selectedProducts: List<ProductUiModel>) {
        this.selectedProducts = selectedProducts.toMutableList()
    }

    // ADAPTER POSITION
    fun setClickedAdapterPosition(adapterPosition: Int) {
        this.adapterPosition = adapterPosition
    }

    fun getClickedAdapterPosition(): Int? {
        return adapterPosition
    }
}