package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
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
        private val validateVoucherUseCase: ValidateVoucherUseCase,
        private val getWarehouseLocationsUseCase: GetWarehouseLocationsUseCase,
        private val getShowCasesByIdUseCase: GetShowCasesByIdUseCase,
        private val getProductListMetaDataUseCase: GetProductListMetaDataUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val FIRST_PAGE = 1
        private const val PAGE_SIZE = 10
        const val SELLER_WAREHOUSE_TYPE = 1
        const val EMPTY_STRING = ""
        const val BENEFIT_TYPE_IDR = "idr"
        const val BENEFIT_TYPE_PERCENT = "percent"
        const val COUPON_TYPE_CASHBACK = "cashback"
        const val COUPON_TYPE_SHIPPING = "shipping"
    }

    private var productUiModels: List<ProductUiModel> = listOf()

    // VOUCHER VALIDATION PROPERTIES
    private var maxProductLimit = 0
    private var couponSettings: CouponSettings? = null
    private var selectedProductIds = listOf<String>()
    private var pageIndex = 1

    // SORT AND FILTER PROPERTIES
    private var searchKeyWord: String? = null
    private var warehouseLocationId: Int? = null
    private var sellerWarehouseId: Int? = null
    private var showCaseSelections = listOf<ShowCaseSelection>()
    private var categorySelections = listOf<CategorySelection>()
    private var selectedSort: GoodsSortInput? = null

    // PRODUCT SELECTIONS
    var isSelectAllMode = true

    // LIVE DATA
    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val productListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData
    private val getWarehouseLocationsResultLiveData = MutableLiveData<Result<ShopLocGetWarehouseByShopIdsResponse>>()
    val getWarehouseLocationsResult: LiveData<Result<ShopLocGetWarehouseByShopIdsResponse>> get() = getWarehouseLocationsResultLiveData
    private val getShowCasesByIdResultLiveData = MutableLiveData<Result<ShopShowcasesByShopIdResponse>>()
    val getShowCasesByIdResult: LiveData<Result<ShopShowcasesByShopIdResponse>> get() = getShowCasesByIdResultLiveData
    private val getProductListMetaDataResultLiveData = MutableLiveData<Result<ProductListMetaResponse>>()
    val getProductListMetaDataResult: LiveData<Result<ProductListMetaResponse>> get() = getProductListMetaDataResultLiveData
    private val validateVoucherResultLiveData = MutableLiveData<Result<VoucherValidationPartialResponse>>()
    val validateVoucherResult: LiveData<Result<VoucherValidationPartialResponse>> get() = validateVoucherResultLiveData

    val selectedProductListLiveData = MutableLiveData<List<ProductUiModel>>(listOf())

    fun getProductList(
            page: Int? = null,
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
                        page = page,
                        pageSize = PAGE_SIZE,
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

    fun validateProductList(benefitType: String,
                            couponType: String,
                            benefitIdr: Int,
                            benefitMax: Int,
                            benefitPercent: Int,
                            minPurchase: Int,
                            productIds: List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = ValidateVoucherUseCase.createRequestParams(
                        benefitType = benefitType,
                        couponType = couponType,
                        benefitIdr = benefitIdr,
                        benefitMax = benefitMax,
                        benefitPercent = benefitPercent,
                        minPurchase = minPurchase,
                        productIds = productIds
                )
                validateVoucherUseCase.setRequestParams(params = params.parameters)
                validateVoucherUseCase.executeOnBackground()
            }
            validateVoucherResultLiveData.value = Success(result)
        }, onError = {
            validateVoucherResultLiveData.value = Fail(it)
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
                    sold = productData.txStats.sold,
                    soldNStock = "Terjual " + productData.txStats.sold + " | " + "Stok " + productData.stock.toString(),
                    hasVariant = productData.isVariant
            )
        }
    }

    fun mapWarehouseLocationToSelections(warehouses: List<Warehouses>): List<WarehouseLocationSelection> {
        return warehouses.map { warehouse ->
            WarehouseLocationSelection(
                    warehouseId = warehouse.warehouseId,
                    warehouseType = warehouse.warehouseType,
                    warehouseName = warehouse.warehouseName,
                    isSelected = warehouse.warehouseType == SELLER_WAREHOUSE_TYPE
            )
        }
    }

    fun getSellerWarehouseId(warehouses: List<Warehouses>): Int {
        return warehouses.first { it.warehouseType == SELLER_WAREHOUSE_TYPE }.warehouseId
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

    fun applyValidationResult(productList: List<ProductUiModel>,
                              validationResults: List<VoucherValidationPartialProduct>): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        validationResults.forEach { validationResult ->
            val productUiModel = mutableProductList.first {
                it.id == validationResult.parentProductId.toString()
            }
            productUiModel.isError = !validationResult.isEligible
            productUiModel.errorMessage = validationResult.reason
            productUiModel.hasVariant = validationResult.isVariant
            productUiModel.variants = mapVariantDataToUiModel(validationResult.variants, productUiModel.sold)
        }
        return mutableProductList.toList()
    }

    private fun mapVariantDataToUiModel(variantValidationData: List<VariantValidationData>, sold: Int): List<VariantUiModel> {
        return variantValidationData.map { data ->
            VariantUiModel(
                    variantId = data.productId.toString(),
                    variantName = data.productName,
                    sku = "SKU : " + data.sku,
                    price = data.price.toString(),
                    priceTxt = data.priceFormat,
                    soldNStock = "Terjual " + sold.toString() + " | " + "Stok " + data.stock.toString(),
                    isError = !data.is_eligible,
                    errorMessage = data.reason
            )
        }
    }

    fun setSellerWarehouseId(warehouseId: Int) {
        this.sellerWarehouseId = warehouseId
    }

    fun setProductUiModels(productUiModels: List<ProductUiModel>) {
        this.productUiModels = productUiModels
    }

    fun getProductUiModels(): List<ProductUiModel> {
        return productUiModels
    }

    fun setSetSelectedProducts(productList: List<ProductUiModel>) {
        this.selectedProductListLiveData.value = productList
    }

    fun getSelectedProducts(): List<ProductUiModel> {
        return selectedProductListLiveData.value?: listOf()
    }

    // SORT AND FILTER
    fun setSearchKeyword(keyword: String) {
        this.searchKeyWord = keyword
    }

    fun getSearchKeyWord(): String? {
        return searchKeyWord
    }

    fun setWarehouseLocationId(warehouseLocation: Int?) {
        this.warehouseLocationId = warehouseLocation
    }

    fun getWarehouseLocationId(): Int? {
        return warehouseLocationId
    }

    fun getSellerWarehouseId(): Int? {
        return sellerWarehouseId
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

    fun setMaxProductLimit(maxProductLimit: Int) {
        this.maxProductLimit = maxProductLimit
    }

    fun setCouponSettings(couponSettings: CouponSettings?) {
        this.couponSettings = couponSettings
    }

    fun getCouponSettings(): CouponSettings? {
        return couponSettings
    }

    fun setSelectedProductIds(selectedProductIds: List<String>) {
        this.selectedProductIds = selectedProductIds
    }

    fun getSelectedProductIds(selectedProducts: ArrayList<ProductUiModel>): List<String> {
        return selectedProducts.map { it.id }.toList()
    }

    fun getSelectedProductIds(): List<String> {
        return selectedProductIds.toList()
    }

    fun getIdsFromProductList(productList: List<ProductUiModel>): List<String> {
        return productList.map { productUiModel ->
            productUiModel.id
        }
    }

    fun getBenefitType(couponSettings: CouponSettings): String {
        return when {
            couponSettings.type == CouponType.FREE_SHIPPING -> BENEFIT_TYPE_IDR
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> BENEFIT_TYPE_IDR
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> BENEFIT_TYPE_PERCENT
            else -> BENEFIT_TYPE_IDR
        }
    }

    fun getCouponType(couponSettings: CouponSettings): String {
        return when (couponSettings.type) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> COUPON_TYPE_CASHBACK
            CouponType.FREE_SHIPPING -> COUPON_TYPE_SHIPPING
        }
    }

    fun getBenefitIdr(couponSettings: CouponSettings): Int {
        return couponSettings.discountAmount
    }

    fun getBenefitMax(couponSettings: CouponSettings): Int {
        return couponSettings.maxDiscount
    }

    fun getBenefitPercent(couponSettings: CouponSettings): Int {
        return couponSettings.discountPercentage
    }

    fun getMinimumPurchase(couponSettings: CouponSettings): Int {
        return couponSettings.minimumPurchase
    }

    fun isMaxProductLimitReached(selectedProductsSize: Int): Boolean {
        return selectedProductsSize > maxProductLimit
    }

    fun isInitialLoad(pageIndex: Int): Boolean {
        return pageIndex == FIRST_PAGE
    }

    fun setPagingIndex(pageIndex: Int) {
        this.pageIndex = pageIndex
    }

    fun getPagingIndex(): Int {
        return pageIndex
    }

    fun excludeSelectedProducts(productList: List<ProductUiModel>,
                                selectedProductIds: List<String>): List<ProductUiModel> {
        return productList.filter { productUiModel ->
            productUiModel.id !in selectedProductIds
        }
    }
}