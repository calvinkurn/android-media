package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.PageMode
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
                _uiState.update { it.copy(voucherConfiguration = event.voucherConfiguration) }
                getProductsAndProductsMetadata(event.pageMode, event.voucherConfiguration)
                getShopShowcases()
            }
            is AddProductEvent.LoadPage -> handleLoadPage(event.page)
            is AddProductEvent.AddProductToSelection -> handleAddProductToSelection(event.productId)
            AddProductEvent.ClearFilter -> handleClearFilter()
            AddProductEvent.ClearSearchBar -> handleClearSearchbar()
            AddProductEvent.ConfirmAddProduct -> handleConfirmAddProduct()
            AddProductEvent.AddNewProducts -> handleAddNewProducts()
            AddProductEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            AddProductEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is AddProductEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.productId)
            AddProductEvent.TapCategoryFilter -> _uiEffect.tryEmit(AddProductEffect.ShowProductCategoryBottomSheet(currentState.categoryOptions, currentState.selectedCategories))
            AddProductEvent.TapWarehouseLocationFilter -> _uiEffect.tryEmit(AddProductEffect.ShowWarehouseLocationBottomSheet(currentState.warehouses, currentState.selectedWarehouseLocation))
            AddProductEvent.TapShowCaseFilter -> _uiEffect.tryEmit(AddProductEffect.ShowShowcasesBottomSheet(currentState.shopShowcases, currentState.selectedShopShowcase.map { it.id }))
            AddProductEvent.TapSortFilter -> _uiEffect.tryEmit(AddProductEffect.ShowSortBottomSheet(currentState.sortOptions, currentState.selectedSort))
            is AddProductEvent.ApplyCategoryFilter -> handleApplyCategoryFilter(event.selectedCategories)
            is AddProductEvent.ApplyWarehouseLocationFilter -> handleApplyWarehouseLocationFilter(event.selectedWarehouseLocation)
            is AddProductEvent.ConfirmChangeWarehouseLocationFilter -> handleConfirmChangeWarehouseLocationFilter(event.newWarehouseLocation)
            is AddProductEvent.ApplyShowCaseFilter -> handleApplyShopShowcasesFilter(event.selectedShowCases)
            is AddProductEvent.ApplySortFilter -> handleApplySortFilter(event.selectedSort)
            is AddProductEvent.SearchProduct -> handleSearchProduct(event.searchKeyword)
            is AddProductEvent.TapVariant -> handleTapVariant(event.parentProduct)
            is AddProductEvent.VariantUpdated -> handleVariantUpdated(event.modifiedParentProductId, event.selectedVariantIds)
        }
    }

    private fun getProductsAndProductsMetadata(pageMode: PageMode, voucherConfiguration: VoucherConfiguration) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action = if (pageMode == PageMode.CREATE) VoucherAction.CREATE else VoucherAction.UPDATE

                val warehouseParam = GetShopWarehouseLocationUseCase.Param()
                val sellerWarehousesDeferred = async { getShopWarehouseLocationUseCase.execute(warehouseParam) }

                val metadataParam = GetInitiateVoucherPageUseCase.Param(action, voucherConfiguration.promoType, isVoucherProduct = true)
                val metadataDeferred = async { getInitiateVoucherPageUseCase.execute(metadataParam) }

                val sellerWarehouses = sellerWarehousesDeferred.await()
                val metadata = metadataDeferred.await()

                val defaultWarehouse = sellerWarehouses.firstOrNull() ?: return@launchCatchError

                val productMetaParams = ProductListMetaUseCase.Param(defaultWarehouse.warehouseId)
                val productListMeta = getProductListMetaUseCase.execute(productMetaParams)

                _uiState.update {
                    it.copy(
                        maxProductSelection = metadata.maxProduct,
                        warehouses = sellerWarehouses,
                        defaultWarehouseLocationId = defaultWarehouse.warehouseId,
                        selectedWarehouseLocation = defaultWarehouse,
                        sortOptions = productListMeta.sortOptions,
                        categoryOptions = productListMeta.categoryOptions,
                        selectedProductsIds = voucherConfiguration.productIds.toSet()
                    )
                }

                getProducts()

            },
            onError = { error ->
                _uiEffect.tryEmit(AddProductEffect.ShowError(error))
                _uiState.update { it.copy(error = error) }
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
                val parentProducts = productsResponse.products.readyStockProductOnly()
                val currentPageParentProductsIds = parentProducts.map { product -> product.id }

                val updatedParentProducts = combineParentProductWithVariantData(
                    parentProducts,
                    currentState.selectedProductsIds,
                    currentState.voucherConfiguration
                )

                val allParentProducts = currentState.products + updatedParentProducts
                val selectedParentProductIds = currentState.selectedProductsIds + allParentProducts.selectedProductsOnly()

                val selectedProductCount = findSelectedProductCount(currentPageParentProductsIds, selectedParentProductIds)

                val checkboxState = determineCheckboxState(selectedProductCount, productsResponse.total)

                _uiEffect.emit(AddProductEffect.LoadNextPageSuccess(allParentProducts.count(), productsResponse.total))
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = allParentProducts,
                        selectedProductsIds = selectedParentProductIds,
                        selectedProductCount = selectedProductCount,
                        checkboxState = checkboxState,
                        totalProducts = productsResponse.total
                    )
                }

            },
            onError = { error ->
                _uiEffect.tryEmit(AddProductEffect.ShowError(error))
                _uiState.update { it.copy(error = error) }
            }
        )

    }

    private suspend fun combineParentProductWithVariantData(
        currentPageParentProduct: List<Product>,
        selectedProductIds: Set<Long>,
        voucherConfiguration: VoucherConfiguration
    ): List<Product> {
        val currentPageParentProductIds =  currentPageParentProduct.map { it.id }

        val voucherValidationParam = VoucherValidationPartialUseCase.Param(
            benefitIdr = voucherConfiguration.benefitIdr,
            benefitMax = voucherConfiguration.benefitMax,
            benefitPercent = voucherConfiguration.benefitPercent,
            benefitType = voucherConfiguration.benefitType,
            promoType = voucherConfiguration.promoType,
            isVoucherProduct = voucherConfiguration.isVoucherProduct,
            minPurchase = voucherConfiguration.minPurchase,
            productIds = currentPageParentProductIds,
            targetBuyer = voucherConfiguration.targetBuyer,
            couponName = voucherConfiguration.voucherName,
            isPublic = voucherConfiguration.isVoucherPublic,
            code = voucherConfiguration.voucherCode,
            isPeriod = voucherConfiguration.isPeriod,
            periodType = voucherConfiguration.periodType,
            periodRepeat = voucherConfiguration.periodRepeat,
            totalPeriod = voucherConfiguration.totalPeriod,
            startDate = voucherConfiguration.startPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
            endDate = voucherConfiguration.endPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
            startHour = voucherConfiguration.startPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION),
            endHour = voucherConfiguration.endPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION)
        )

        val validatedProducts = voucherValidationPartialUseCase.execute(voucherValidationParam).validationProduct

        return currentPageParentProduct.map { product ->
            val validatedParentProduct = findValidatedProduct(product.id, validatedProducts)
            val variants = validatedParentProduct.toProductVariants()

            val isEligible = validatedParentProduct?.isEligible.orTrue()

            val isSelected = shouldSelectProduct(product.id, selectedProductIds, isEligible)
            val enableCheckbox = shouldEnableCheckbox(isSelected, isEligible, selectedProductIds.size, currentState.maxProductSelection)

            product.copy(
                isEligible = isEligible,
                ineligibleReason = validatedParentProduct?.reason.orEmpty(),
                originalVariants = variants,
                selectedVariantsIds = variants.allVariantIds(),
                isSelected = isSelected,
                enableCheckbox = enableCheckbox
            )
        }
    }

    private fun shouldSelectProduct(productId: Long, selectedProductIds: Set<Long>, isEligible: Boolean): Boolean {
        val nextSelectedProductSize = selectedProductIds.size + AddProductFragment.PAGE_SIZE
        val isBelowMaximumProductSelection = nextSelectedProductSize <= currentState.maxProductSelection
        val shouldAutomaticallyAddToProductSelection = currentState.checkboxState.isChecked()  && isBelowMaximumProductSelection

        val isProductAlreadyOnSelection = productId in selectedProductIds

        if (isProductAlreadyOnSelection) {
            return true
        }

        if (!isEligible) {
            return false
        }

        if (shouldAutomaticallyAddToProductSelection) {
            return true
        }

        return false
    }

    private fun shouldEnableCheckbox(
        isSelected: Boolean,
        isEligible: Boolean,
        totalSelectedProductCount: Int,
        maxProductSelection: Int
    ): Boolean {
        if (isSelected) {
            return true
        }

        if (!isEligible) {
            return false
        }

        if (totalSelectedProductCount < maxProductSelection) {
            return true
        }

        return false
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
        val isOnSearchMode = currentState.searchKeyword.isNotEmpty()
        val currentlyDisplayedProductIds = currentState.products.map { it.id }

        val maxProductSelection = currentState.maxProductSelection

        val modifiedProducts = currentState.products.mapIndexed { index, product ->
            val isProductOnAutoSelectRange = index < maxProductSelection

            determineShouldSelectProduct(
                product,
                currentlyDisplayedProductIds,
                isOnSearchMode,
                isProductOnAutoSelectRange
            )
        }

        _uiState.update {
            it.copy(
                checkboxState = AddProductUiState.CheckboxState.CHECKED,
                selectedProductsIds = modifiedProducts.selectedProductsOnly(),
                selectedProductCount = modifiedProducts.selectedProductsOnly().count(),
                products = modifiedProducts
            )
        }
    }

    private fun handleUncheckAllProduct() = launch(dispatchers.computation) {
        val disabledProducts = currentState.products.map { it.copy(isSelected = false, enableCheckbox = true) }
        _uiState.update {
            it.copy(
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                selectedProductsIds = emptySet(),
                selectedProductCount = 0,
                products = disabledProducts
            )
        }
    }

    private fun handleAddProductToSelection(productIdToAdd: Long) = launch(dispatchers.computation) {
        val maxProductSelection = currentState.maxProductSelection
        val newSelectedProductCountAfterSelection = currentState.selectedProductsIds.size.inc()
        val isReachedMaxSelection = newSelectedProductCountAfterSelection == maxProductSelection

        val updatedProducts = if (isReachedMaxSelection) {
            //Disable unselected products
            val disabledProducts = disableUnselectedProducts(currentState.products)
            updateProductAsSelected(productIdToAdd, disabledProducts)
        } else {
            updateProductAsSelected(productIdToAdd, currentState.products)
        }

        val updatedSelectedProductIds = currentState.selectedProductsIds + setOf(productIdToAdd)

        val allParentProductSelected = updatedSelectedProductIds.count() == currentState.totalProducts

        val checkBoxState = if (allParentProductSelected) {
            AddProductUiState.CheckboxState.CHECKED
        } else {
            AddProductUiState.CheckboxState.INDETERMINATE
        }

        _uiState.update {
            it.copy(
                selectedProductsIds = updatedSelectedProductIds,
                selectedProductCount = updatedSelectedProductIds.count(),
                products = updatedProducts,
                checkboxState = checkBoxState
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

    private fun updateProductAsSelected(selectedProductId: Long, products: List<Product>) : List<Product> {
        return products.map {
            if (it.id == selectedProductId) {

                val hasVariants = it.originalVariants.isNotEmpty()
                if (hasVariants) {
                    val eligibleVariantsOnly = it.originalVariants.eligibleVariantIdsOnly()
                    it.copy(
                        isSelected = true,
                        enableCheckbox = true,
                        originalVariants = it.originalVariants,
                        selectedVariantsIds = eligibleVariantsOnly
                    )
                } else {
                    it.copy(isSelected = true, enableCheckbox = true)
                }

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
                    it.copy(isSelected = false, enableCheckbox = true, originalVariants = it.originalVariants, selectedVariantsIds = it.originalVariants.map { it.variantProductId }.toSet())
                } else {
                    it.copy(isSelected = false, enableCheckbox = true)
                }

            } else {
                it.copy(enableCheckbox = true)
            }
        }

        val updatedSelectedProducts = currentState.selectedProductsIds.toMutableSet()
        updatedSelectedProducts.remove(productIdToDelete)

        val checkBoxState = if (updatedSelectedProducts.isEmpty()) {
            AddProductUiState.CheckboxState.UNCHECKED
        } else {
            AddProductUiState.CheckboxState.INDETERMINATE
        }

        _uiState.update {
            it.copy(
                checkboxState = checkBoxState,
                selectedProductsIds = updatedSelectedProducts,
                selectedProductCount = updatedSelectedProducts.count(),
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
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
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
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                selectedCategories = emptyList(),
                selectedWarehouseLocation = Warehouse(currentState.defaultWarehouseLocationId, "", WarehouseType.DEFAULT_WAREHOUSE_LOCATION),
                selectedShopShowcase = emptyList(),
                selectedSort = ProductSortOptions("DEFAULT", "", "DESC"),
                isFilterActive = false
            )
        }

        getProducts()
    }

    private fun handleSearchProduct(searchKeyword : String) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                products = emptyList(),
                searchKeyword = searchKeyword
            )
        }

        getProducts()
    }

    private fun handleApplySortFilter(selectedSort: ProductSortOptions) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                products = emptyList(),
                selectedSort = selectedSort,
                isFilterActive = selectedSort.id != "DEFAULT"
            )
        }

        getProducts()
    }

    private fun handleApplyWarehouseLocationFilter(selectedWarehouse: Warehouse) {
        val currentWarehouseId = currentState.selectedWarehouseLocation.warehouseId
        val newWarehouseId = selectedWarehouse.warehouseId
        val isWarehouseChanged = currentWarehouseId != newWarehouseId

        if (isWarehouseChanged) {
            _uiEffect.tryEmit(AddProductEffect.ShowChangeWarehouseDialogConfirmation(selectedWarehouse))
        } else {
            handleConfirmChangeWarehouseLocationFilter(selectedWarehouse)
        }
    }


    private fun handleConfirmChangeWarehouseLocationFilter(newWarehouseLocation: Warehouse) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                products = emptyList(),
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                selectedProductsIds = emptySet(),
                selectedProductCount = 0,
                selectedWarehouseLocation = newWarehouseLocation,
                isFilterActive = newWarehouseLocation.warehouseId != currentState.defaultWarehouseLocationId
            )
        }

        getProducts()
    }

    private fun handleApplyShopShowcasesFilter(selectedShopShowcases: List<ShopShowcase>) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                products = emptyList(),
                selectedShopShowcase = selectedShopShowcases,
                isFilterActive = selectedShopShowcases.isNotEmpty()
            )
        }

        getProducts()
    }

    private fun handleApplyCategoryFilter(selectedCategories: List<ProductCategoryOption>) {
        _uiState.update {
            it.copy(
                isLoading = true,
                page = NumberConstant.FIRST_PAGE,
                checkboxState = AddProductUiState.CheckboxState.UNCHECKED,
                products = emptyList(),
                selectedCategories = selectedCategories,
                isFilterActive = selectedCategories.isNotEmpty()
            )
        }

        getProducts()
    }

    private fun handleTapVariant(parentProduct: Product) {
        _uiEffect.tryEmit(AddProductEffect.ShowVariantBottomSheet(parentProduct))
    }

    private fun handleVariantUpdated(parentProductId: Long, selectedVariantIds : Set<Long>) {
        launch(dispatchers.computation) {

            val updatedProducts = currentState.products.map { parentProduct ->
                if (parentProduct.id == parentProductId) {
                    parentProduct.copy(selectedVariantsIds = selectedVariantIds)
                } else {
                    parentProduct
                }
            }

            _uiState.update { it.copy(products = updatedProducts) }
        }
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
                _uiEffect.tryEmit(AddProductEffect.ShowError(error))
                _uiState.update { it.copy(error = error) }
            }
        )
    }


    private fun handleConfirmAddProduct() {
        launch(dispatchers.computation) {
            val selectedProducts = currentState.products
                .filter { it.isSelected }
                .map { product ->
                    val parentProductId = product.id
                    val variantProductIds = product.selectedVariantsIds.toList()
                    SelectedProduct(parentProductId, variantProductIds)
                }

            val topSellingProductImageUrls = currentState.products
                .filter { it.isSelected }
                .sortedByDescending { it.txStats.sold }
                .map { it.picture }


            _uiEffect.tryEmit(
                AddProductEffect.ProductConfirmed(
                    selectedProducts,
                    topSellingProductImageUrls,
                    currentState.voucherConfiguration,
                    currentState.selectedWarehouseLocation.warehouseId
                )
            )
        }
    }

    private fun handleAddNewProducts() {
        launch(dispatchers.computation) {
            val selectedProducts = currentState.products.filter { it.isSelected }

            val topSellingProductImageUrls = selectedProducts
                .sortedByDescending { it.txStats.sold }
                .map { it.picture }


            _uiEffect.tryEmit(AddProductEffect.AddNewProducts(selectedProducts, topSellingProductImageUrls))
        }
    }


    private fun List<Product>.selectedProductsOnly(): Set<Long> {
        return filter { it.isSelected }.map { it.id }.toSet()
    }

    private fun VoucherValidationResult.ValidationProduct?.toProductVariants(): List<Product.Variant> {
        if (this == null) return emptyList()

        return variant.map {
            Product.Variant(
                variantProductId = it.productId,
                isEligible = it.isEligible,
                reason = it.reason,
                isSelected = it.isEligible,
            )
        }
    }

    private fun List<Product.Variant>.eligibleVariantIdsOnly(): Set<Long> {
        return filter { variant -> variant.isEligible }
            .map { variant -> variant.variantProductId }
            .toSet()
    }

    private fun List<Product.Variant>.allVariantIds(): Set<Long> {
        return map { it.variantProductId }.toSet()
    }

    private fun AddProductUiState.CheckboxState.isChecked(): Boolean {
        return this == AddProductUiState.CheckboxState.CHECKED
    }

    private fun List<Product>.readyStockProductOnly(): List<Product> {
        return filter { it.preorder.durationDays.isZero() }
    }

    private fun findSelectedProductCount(
        currentPageParentProductsIds: List<Long>,
        selectedParentProductIds: Set<Long>
    ): Int {
        val isOnSearchMode = currentState.searchKeyword.isNotEmpty()
        val selectedProductCount = if (isOnSearchMode) {
            currentPageParentProductsIds.count { it in selectedParentProductIds }
        } else {
            selectedParentProductIds.count()
        }
        return selectedProductCount
    }

    private fun determineCheckboxState(
        selectedProductCount: Int,
        totalProductCount: Int
    ): AddProductUiState.CheckboxState {
        val checkboxState = when {
            currentState.checkboxState.isChecked() -> AddProductUiState.CheckboxState.CHECKED
            selectedProductCount.isZero() -> AddProductUiState.CheckboxState.UNCHECKED
            selectedProductCount < totalProductCount -> AddProductUiState.CheckboxState.INDETERMINATE
            selectedProductCount == totalProductCount -> AddProductUiState.CheckboxState.CHECKED
            else -> AddProductUiState.CheckboxState.UNCHECKED
        }
        return checkboxState
    }

    private fun determineShouldSelectProduct(
        product: Product,
        currentlyDisplayedProductIds: List<Long>,
        isOnSearchMode: Boolean,
        isProductOnAutoSelectRange: Boolean
    ): Product {
        return when {
            !product.isEligible -> {
                product.copy(isSelected = false, enableCheckbox = false)
            }
            isProductOnAutoSelectRange -> {

                val isProductDisplayedOnSearchResult = product.id in currentlyDisplayedProductIds
                val isSelected = if (isOnSearchMode) {
                    //If is on search product mode. Only product from search result should be selected
                    isProductDisplayedOnSearchResult
                } else {
                    //If we're not in search product mode. Select all loaded products
                    true
                }

                //To make sure only eligible variant will be selected
                val eligibleVariantIdsOnly = product.originalVariants.eligibleVariantIdsOnly()

                product.copy(
                    isSelected = isSelected,
                    enableCheckbox = true,
                    selectedVariantsIds = eligibleVariantIdsOnly
                )
            }
            !isProductOnAutoSelectRange -> {
                product.copy(isSelected = false, enableCheckbox = false)
            }
            else -> product
        }
    }
}
