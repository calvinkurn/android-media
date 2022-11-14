package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEffect
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getShopWarehouseLocationUseCase: GetShopWarehouseLocationUseCase,
    private val getShopShowcasesByShopIDUseCase: ShopShowcasesByShopIDUseCase,
    private val getProductListMetaUseCase: ProductListMetaUseCase,
    private val getProductsUseCase: ProductListUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AddProductEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: AddProductUiState
        get() = _uiState.value

    fun processEvent(event: AddProductEvent) {
        when(event) {
            is AddProductEvent.FetchRequiredData -> {
                getProductsAndProductsMetadata(event.action, event.promoType)
                getSortAndCategoryFilter()
                getShopShowcases()
            }
            is AddProductEvent.LoadPage -> handleLoadPage(event.page)
            is AddProductEvent.AddProductToSelection -> handleAddProductToSelection(event.productId)
            AddProductEvent.ClearFilter -> handleClearFilter()
            AddProductEvent.ClearSearchBar -> handleClearSearchbar()
            AddProductEvent.ConfirmAddProduct -> {}
            AddProductEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            AddProductEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is AddProductEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.productId)
            AddProductEvent.TapCategoryFilter -> _uiEffect.tryEmit(AddProductEffect.ShowProductCategoryBottomSheet(currentState.categoryOptions, currentState.selectedCategories))
            AddProductEvent.TapLocationFilter -> _uiEffect.tryEmit(AddProductEffect.ShowWarehouseLocationBottomSheet(currentState.warehouses, currentState.selectedWarehouseLocation))
            AddProductEvent.TapShowCaseFilter -> _uiEffect.tryEmit(AddProductEffect.ShowShowcasesBottomSheet(currentState.shopShowcases, currentState.selectedShopShowcase))
            AddProductEvent.TapSortFilter -> _uiEffect.tryEmit(AddProductEffect.ShowSortBottomSheet(currentState.sortOptions, currentState.selectedSort))
            is AddProductEvent.ApplyCategoryFilter -> handleApplyCategoryFilter(event.selectedCategories)
            is AddProductEvent.ApplyWarehouseLocationFilter -> handleApplyWarehouseLocationFilter(event.selectedWarehouseLocation)
            is AddProductEvent.ApplyShowCaseFilter -> handleApplyShopShowcasesFilter(event.selectedShowCases)
            is AddProductEvent.ApplySortFilter -> handleApplySortFilter(event.selectedSort)
        }
    }


    private fun getProductsAndProductsMetadata(action: VoucherAction, promoType: PromoType, ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val warehouseParam = GetShopWarehouseLocationUseCase.Param()
                val sellerWarehousesDeferred = async { getShopWarehouseLocationUseCase.execute(warehouseParam) }

                val metadataParam = GetInitiateVoucherPageUseCase.Param(action, promoType, isVoucherProduct = true)
                val metadataDeferred = async { getInitiateVoucherPageUseCase.execute(metadataParam) }

                val sellerWarehouses = sellerWarehousesDeferred.await()
                val metadata = metadataDeferred.await()

                val defaultWarehouse = sellerWarehouses.firstOrNull() ?: return@launchCatchError

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        voucherCreationMetadata = metadata,
                        warehouses = sellerWarehouses,
                        selectedWarehouseLocation = defaultWarehouse
                    )
                }

                getProducts()

            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun getProducts() {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductListUseCase.Param(
                    searchKeyword = currentState.searchKeyword,
                    warehouseId = currentState.selectedWarehouseLocation.warehouseId,
                    categoryIds = currentState.selectedCategories.map { it.id.toLong() },
                    showcaseIds = currentState.selectedShopShowcase.map { it.id },
                    page = currentState.page,
                    pageSize = AddProductFragment.PAGE_SIZE,
                    sortId = currentState.selectedSort.id,
                    sortDirection = currentState.selectedSort.value
                )

                val productsResponse = getProductsUseCase.execute(param)
                val currentPageParentProductsResponse = productsResponse.products
                val currentPageParentProductsIds = currentPageParentProductsResponse.map { product -> product.id }

                val nextSelectedProductSize = currentState.selectedProductsIds.size + AddProductFragment.PAGE_SIZE
                val isBelowMaximumProductSelection = nextSelectedProductSize <= currentState.voucherCreationMetadata?.maxProduct.orZero()
                val enableAutoSelect = currentState.isSelectAllActive && isBelowMaximumProductSelection

                val selectedProductIds = if (enableAutoSelect) {
                    //If select all active, products from new page should be auto checked
                    currentState.selectedProductsIds + currentPageParentProductsIds
                } else {
                    currentState.selectedProductsIds
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedProductsIds = selectedProductIds,
                    )
                }

                val voucherValidationParam = VoucherValidationPartialUseCase.Param(
                    benefitIdr = 25_000,
                    benefitMax = 500_000,
                    benefitPercent = 0,
                    BenefitType.NOMINAL,
                    PromoType.CASHBACK,
                    isLockToProduct = true,
                    minPurchase = 50_000,
                    productIds = currentPageParentProductsIds
                )


                val voucherValidationResponse = voucherValidationPartialUseCase.execute(voucherValidationParam)

                val updatedProducts = combineParentProductDataWithVariant(
                    currentPageParentProductsResponse,
                    voucherValidationResponse.validationProduct,
                    isBelowMaximumProductSelection
                )


                val allProducts = currentState.products + updatedProducts
                _uiEffect.emit(AddProductEffect.LoadNextPageSuccess(updatedProducts, allProducts))

                _uiState.update {
                    it.copy(products = allProducts, totalProducts = productsResponse.total)
                }

            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            }
        )

    }


    private fun combineParentProductDataWithVariant(
        currentPageParentProduct: List<Product>,
        validatedProducts: List<VoucherValidationResult.ValidationProduct>,
        isBelowMaxSelection: Boolean
    ): List<Product> {
        val formattedProducts = currentPageParentProduct.map { product ->

            val matchedProduct = findValidatedProduct(product.id, validatedProducts)
            val variants = matchedProduct?.variant?.map { Product.Variant(it.productId) }
            val isProductAlreadyOnSelection = product.id in currentState.selectedProductsIds

            product.copy(
                isEligible = matchedProduct?.isEligible.orTrue(),
                ineligibleReason = matchedProduct?.reason.orEmpty(),
                originalVariants = variants.orEmpty(),
                modifiedVariants = variants.orEmpty(),
                isSelected = isProductAlreadyOnSelection,
                enableCheckbox = isBelowMaxSelection
            )
        }

        return formattedProducts
    }

    private fun findValidatedProduct(
        productId: Long,
        products: List<VoucherValidationResult.ValidationProduct>
    ): VoucherValidationResult.ValidationProduct? {
        return products.find { it.parentProductId == productId }
    }


    private fun handleLoadPage(nextPage : Int) {
        _uiState.update { it.copy(page = nextPage) }
        getProducts()
    }

    private fun handleCheckAllProduct() = launch(dispatchers.computation) {
        val maxProductSelection = currentState.voucherCreationMetadata?.maxProduct.orZero()
        val selectedProducts = currentState.products.mapIndexed { index, product ->
            if (index < maxProductSelection) {
                product.copy(isSelected = true, enableCheckbox = true)
            } else {
                product.copy(isSelected = false, enableCheckbox = false)
            }
        }

        val selectedProductIds = selectedProducts.filter { it.isSelected }.map { it.id }

        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                selectedProductsIds = selectedProductIds,
                products = selectedProducts
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val disabledProducts = currentState.products.map { it.copy(isSelected = false, enableCheckbox = true) }
        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = emptyList(),
                products = disabledProducts
            )
        }
    }

    private fun handleAddProductToSelection(productIdToAdd: Long) = launch(dispatchers.computation) {
        val maxProductSelection = currentState.voucherCreationMetadata?.maxProduct.orZero()
        val newSelectedProductCountAfterSelection = currentState.selectedProductsIds.size.inc()
        val isReachedMaxSelection = newSelectedProductCountAfterSelection == maxProductSelection

        val updatedProducts = if (isReachedMaxSelection) {
            //Disable unselected products
            val disabledProducts = disableUnselectedProducts(currentState.products)
            updateProductAsSelected(productIdToAdd, disabledProducts)
        } else {
            updateProductAsSelected(productIdToAdd, currentState.products)
        }

        val updatedProductIds = updatedProducts.filter { it.isSelected }.map { it.id }

        _uiState.update {
            it.copy(
                selectedProductsIds = updatedProductIds,
                products = updatedProducts
            )
        }
    }

    private fun disableUnselectedProducts(products: List<Product>): List<Product> {
        return products.map {
            if (it.isSelected) {
                it.copy(isSelected = true, enableCheckbox = true)
            } else {
               it.copy(isSelected = false, enableCheckbox = false)
            }
        }
    }

    private fun updateProductAsSelected(selectedProductId: Long, products: List<Product>,) : List<Product> {
        return products.map {
            if (it.id == selectedProductId) {
                it.copy(isSelected = true, enableCheckbox = true)
            } else {
                it
            }
        }
    }

    private fun handleRemoveProductFromSelection(productIdToDelete: Long) = launch(dispatchers.computation) {
        val updatedProducts = currentState.products.map {
            if (it.id == productIdToDelete) {

                val hasVariants = it.originalVariants.isNotEmpty()
                if (hasVariants) {
                    it.copy(isSelected = false, enableCheckbox = true, originalVariants = it.originalVariants, modifiedVariants = it.originalVariants)
                } else {
                    it.copy(isSelected = false, enableCheckbox = true)
                }

            } else {
                it.copy(enableCheckbox = true)
            }
        }

        val updatedSelectedProducts = currentState.selectedProductsIds.toMutableList()
        updatedSelectedProducts.remove(productIdToDelete)

        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = updatedSelectedProducts,
                products = updatedProducts
            )
        }
    }


    private fun handleClearSearchbar() {
        _uiState.update {
            it.copy(
                isLoading = true,
                searchKeyword = "",
                page = NumberConstant.FIRST_PAGE,
                products = emptyList()
            )
        }
        getProducts()
    }

    private fun handleClearFilter() {
        _uiState.update {
            it.copy(
                isLoading = true,
                searchKeyword = currentState.searchKeyword,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                selectedCategories = emptyList(),
                selectedWarehouseLocation = Warehouse(0, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION),
                selectedShopShowcase = emptyList(),
                selectedSort = ProductSortOptions("DEFAULT", "", "DESC")
            )
        }

        getProducts()
    }

    private fun handleApplySortFilter(selectedSort: ProductSortOptions) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                selectedSort = selectedSort
            )
        }

        getProducts()
    }

    private fun handleApplyWarehouseLocationFilter(selectedWarehouse: Warehouse) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                selectedWarehouseLocation = selectedWarehouse
            )
        }

        getProducts()
    }

    private fun handleApplyShopShowcasesFilter(selectedShopShowcases: List<ShopShowcase>) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                selectedShopShowcase = selectedShopShowcases
            )
        }

        getProducts()
    }

    private fun handleApplyCategoryFilter(selectedCategories: List<ProductCategoryOption>) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                selectedCategories = selectedCategories
            )
        }

        getProducts()
    }

    private fun getSortAndCategoryFilter() {
        launchCatchError(
            dispatchers.io,
            block = {

                val param = ProductListMetaUseCase.Param(0)
                val productListMeta = getProductListMetaUseCase.execute(param)

                _uiState.update {
                    it.copy(
                        sortOptions = productListMeta.sortOptions,
                        categoryOptions = productListMeta.categoryOptions,
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun getShopShowcases() {
        launchCatchError(
            dispatchers.io,
            block = {
                val shopShowcases = getShopShowcasesByShopIDUseCase.execute()
                val sellerCreatedShowcasesOnly = shopShowcases.filter { shopShowcase -> shopShowcase.type != NumberConstant.ID_TOKOPEDIA_CREATED_SHOWCASE_TYPE }
                _uiState.update { it.copy(shopShowcases = sellerCreatedShowcasesOnly) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }
}
