package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetShopWarehouseLocationUseCase
import com.tokopedia.mvc.domain.usecase.ProductListMetaUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ProductV3UseCase
import com.tokopedia.mvc.domain.usecase.ShopShowcasesByShopIDUseCase
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
    private val getProductVariant: ProductV3UseCase
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
            is AddProductEvent.AddProductToSelection -> TODO()
            AddProductEvent.ApplyCategoryFilter -> TODO()
            AddProductEvent.ApplyLocationFilter -> TODO()
            AddProductEvent.ApplyShowCaseFilter -> TODO()
            AddProductEvent.ApplySortFilter -> TODO()
            AddProductEvent.ClearFilter -> TODO()
            AddProductEvent.ClearSearchBar -> TODO()
            AddProductEvent.ConfirmAddProduct -> TODO()
            AddProductEvent.DisableSelectAllCheckbox -> TODO()
            AddProductEvent.EnableSelectAllCheckbox -> TODO()
            is AddProductEvent.RemoveProductFromSelection -> TODO()
            AddProductEvent.TapCategoryFilter -> TODO()
            AddProductEvent.TapLocationFilter -> TODO()
            AddProductEvent.TapShowCaseFilter -> TODO()
            AddProductEvent.TapSortFilter -> TODO()
        }
    }

    private fun fetchRequiredData(
        action: GetInitiateVoucherPageUseCase.Param.Action,
        promoType: GetInitiateVoucherPageUseCase.Param.PromoType,
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
                val products = getProductsUseCase.execute(param)

                val allProducts = currentState.products + products

                _uiEffect.emit(AddProductEffect.LoadNextPageSuccess(products, allProducts))

                _uiState.update {
                    it.copy(products = allProducts)
                }

            },
            onError = { error ->

            }
        )

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
