package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
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
    private val getProductVariant: ProductV3UseCase,
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
            is AddProductEvent.FetchRequiredData -> fetchRequiredData(event.action, event.promoType)
            is AddProductEvent.LoadPage -> getProducts(event.warehouseId, event.page, event.sortId, event.sortDirection)
            is AddProductEvent.AddProductToSelection -> handleAddProductToSelection(event.productId)
            AddProductEvent.ApplyCategoryFilter -> {}
            AddProductEvent.ApplyLocationFilter -> {}
            AddProductEvent.ApplyShowCaseFilter -> {}
            AddProductEvent.ApplySortFilter -> {}
            AddProductEvent.ClearFilter -> {
                _uiState.update { it.copy(products = emptyList()) }
                getProducts(0L, 0, "DEFAULT", "DESC")
            }
            AddProductEvent.ClearSearchBar -> {
                _uiState.update { it.copy(products = emptyList()) }
                getProducts(0L, 0, "DEFAULT", "DESC")
            }
            AddProductEvent.ConfirmAddProduct -> {}
            AddProductEvent.DisableSelectAllCheckbox -> handleUncheckAllProduct()
            AddProductEvent.EnableSelectAllCheckbox -> handleCheckAllProduct()
            is AddProductEvent.RemoveProductFromSelection -> handleRemoveProductFromSelection(event.productId)
            AddProductEvent.TapCategoryFilter -> {}
            AddProductEvent.TapLocationFilter -> {}
            AddProductEvent.TapShowCaseFilter -> {}
            AddProductEvent.TapSortFilter -> {}
        }
    }


    private fun fetchRequiredData(
        action: VoucherAction,
        promoType: PromoType,
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val warehouseParam = GetShopWarehouseLocationUseCase.Param()
                val sellerWarehousesDeferred = async { getShopWarehouseLocationUseCase.execute(warehouseParam) }

                val metadataParam = GetInitiateVoucherPageUseCase.Param(action, promoType, isVoucherProduct = true)
                val metadataDeferred = async { getInitiateVoucherPageUseCase.execute(metadataParam) }

                val sellerWarehouses = sellerWarehousesDeferred.await()
                val metadata = metadataDeferred.await()

                val defaultWarehouseId = sellerWarehouses.firstOrNull()?.warehouseId.orZero()

                val param = ProductListMetaUseCase.Param(defaultWarehouseId)
                val productsMetaDeferred = async { getProductListMetaUseCase.execute(param) }

                val shopShowcasesDeferred = async { getShopShowcasesByShopIDUseCase.execute() }

                val productListMeta = productsMetaDeferred.await()
                val shopShowCases = shopShowcasesDeferred.await()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        voucherCreationMetadata = metadata,
                        warehouses = sellerWarehouses,
                        sortOptions = productListMeta.sortOptions,
                        categoryOptions = productListMeta.categoryOptions,
                        shopShowcases = shopShowCases
                    )
                }

                getProducts(defaultWarehouseId, 1, "DEFAULT", "DESC")

            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            }
        )
    }

    private fun getProducts(
        warehouseId: Long,
        page: Int,
        sortId: String,
        sortDirection: String
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductListUseCase.Param(
                    warehouseId = warehouseId,
                    page = page,
                    pageSize = AddProductFragment.PAGE_SIZE,
                    sortId = sortId,
                    sortDirection = sortDirection
                )

                val currentPageParentProductsResponse = getProductsUseCase.execute(param)
                val currentPageParentProductsIds = currentPageParentProductsResponse.map { product -> product.id }

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

                val selectedProducts = if (currentState.isSelectAllActive) {
                    //If select all active, products from new page should be auto checked
                    currentState.selectedProductsIds + currentPageParentProductsIds
                } else {
                    currentState.selectedProductsIds
                }

                _uiState.update {
                    it.copy(products = allProducts, selectedProductsIds = selectedProducts)
                }

            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            }
        )

    }

    private fun combineParentProductDataWithVariant(currentPageParentProduct: List<Product>, validatedProducts: List<VoucherValidationResult.ValidationProduct>): List<Product> {
        val formattedProducts = currentPageParentProduct.map { product ->

            val matchedProduct = findValidatedProduct(product.id, validatedProducts)
            val variants = matchedProduct?.variant?.map { Product.Variant(it.productId) }

            product.copy(
                isEligible = matchedProduct?.isEligible.orTrue(),
                ineligibleReason = matchedProduct?.reason.orEmpty(),
                variants = variants.orEmpty(),
                isSelected = currentState.isSelectAllActive
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
            if (it.id == productIdToAdd) {
                it.copy(isSelected = true)
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
                it.copy(isSelected = false)
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

    fun getProductVariants(productId:Long, warehouseId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = ProductV3UseCase.Param(productId, warehouseId)
                val response = getProductVariant.execute(param)
                println()
            },
            onError = { error ->

            }
        )

    }
}
