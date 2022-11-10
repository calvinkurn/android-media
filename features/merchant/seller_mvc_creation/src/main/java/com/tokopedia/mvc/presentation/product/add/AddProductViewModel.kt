package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orTrue
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
                    page = currentState.page,
                    pageSize = AddProductFragment.PAGE_SIZE,
                    sortId = currentState.selectedSort.id,
                    sortDirection = currentState.selectedSort.value
                )

                val productsResponse = getProductsUseCase.execute(param)
                val currentPageParentProductsResponse = productsResponse.products
                val currentPageParentProductsIds = currentPageParentProductsResponse.map { product -> product.id }

                val selectedProductIds = if (currentState.isSelectAllActive) {
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
                    voucherValidationResponse.validationProduct
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
        validatedProducts: List<VoucherValidationResult.ValidationProduct>
    ): List<Product> {
        val formattedProducts = currentPageParentProduct.map { product ->

            val matchedProduct = findValidatedProduct(product.id, validatedProducts)
            val variants = matchedProduct?.variant?.map { Product.Variant(it.productId) }
            val isProductAlreadySelected = product.id in currentState.selectedProductsIds
            val isSelected = isProductAlreadySelected || currentState.isSelectAllActive

            product.copy(
                isEligible = matchedProduct?.isEligible.orTrue(),
                ineligibleReason = matchedProduct?.reason.orEmpty(),
                originalVariants = variants.orEmpty(),
                modifiedVariants = variants.orEmpty(),
                isSelected = isSelected
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

    private fun handleCheckAllProduct() {
        val enabledProducts = currentState.products.map { it.copy(isSelected = true) }
        _uiState.update {
            it.copy(
                isSelectAllActive = true,
                selectedProductsIds = enabledProducts.map { it.id },
                products = enabledProducts
            )
        }
    }

    private fun handleUncheckAllProduct() {
        val disabledProducts = currentState.products.map { it.copy(isSelected = false) }
        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = emptyList(),
                products = disabledProducts
            )
        }
    }

    private fun handleAddProductToSelection(productIdToAdd: Long) {
        val updatedProducts = currentState.products.map {
            val hasVariants = it.originalVariants.isNotEmpty()

            if (it.id == productIdToAdd) {

                if (hasVariants) {
                    val modifiedVariants = it.originalVariants.toMutableList()
                    modifiedVariants.removeLast()
                    it.copy(isSelected = true, originalVariants = it.originalVariants, modifiedVariants = modifiedVariants)
                } else {
                    it.copy(isSelected = true)
                }

            } else {
                it
            }
        }

        val updatedSelectedProducts = currentState.selectedProductsIds.toMutableList()
        updatedSelectedProducts.add(productIdToAdd)


        _uiState.update {
            it.copy(
                isSelectAllActive = false,
                selectedProductsIds = updatedSelectedProducts,
                products = updatedProducts
            )
        }
    }

    private fun handleRemoveProductFromSelection(productIdToDelete: Long) {
        val updatedProducts = currentState.products.map {
            if (it.id == productIdToDelete) {

                val hasVariants = it.originalVariants.isNotEmpty()
                if (hasVariants) {
                    it.copy(isSelected = false, originalVariants = it.originalVariants, modifiedVariants = it.originalVariants)
                } else {
                    it.copy(isSelected = false)
                }

            } else {
                it
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
