package com.tokopedia.tkpd.flashsale.presentation.list

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductSubmissionRequest
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductDeleteUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductSubmissionUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleReservedProductListUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LandingContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase,
    private val doFlashSaleProductSubmissionUseCase: DoFlashSaleProductSubmissionUseCase,
    private val doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase,
    private val getFlashSaleReservedProductListUseCase: GetFlashSaleReservedProductListUseCase,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase
) : BaseViewModel(dispatchers.main) {

    data class UiState(
        val isLoading: Boolean = false,
        val tabsMetadata: List<TabMetadata> = emptyList(),
        val targetTabPosition: Int = 0,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data class FetchTabMetaError(val throwable: Throwable) : UiEvent()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(replay = 1)
    val uiEvent = _uiEvent.asSharedFlow()


    fun getTabsMetaData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val response = getFlashSaleListForSellerMetaUseCase.execute()

                _uiEvent.tryEmit(UiEvent.FetchTabMetaError(MessageErrorException("Some error message")))
                _uiState.update { it.copy(tabsMetadata = response) }

            },
            onError = { error ->
                _uiEvent.emit(UiEvent.FetchTabMetaError(error))
            }
        )

    }

    fun submitProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val warehouses = listOf(
                    DoFlashSaleProductSubmissionRequest.ProductData.Warehouse(12181276, 4000, 1)
                )
                val productData = listOf(
                    DoFlashSaleProductSubmissionRequest.ProductData(10393, 5338343889, warehouses)
                )
                val params = DoFlashSaleProductSubmissionUseCase.Param(
                    938228,
                    productData,
                    "122402171660132340376"
                )
                val response = doFlashSaleProductSubmissionUseCase.execute(params)


            },
            onError = { error ->

            }
        )

    }

    fun deleteProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = DoFlashSaleProductDeleteUseCase.Param(
                    938228,
                    listOf(1, 2, 3, 4),
                    "122402171660144143955"
                )
                val response = doFlashSaleProductDeleteUseCase.execute(params)
            },
            onError = { error ->
            }
        )

    }

    fun getReservedProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params =
                    GetFlashSaleReservedProductListUseCase.Param("122402171660144143955", 0)
                val response = getFlashSaleReservedProductListUseCase.execute(params)
            },
            onError = { error ->
            }
        )

    }

    fun getFlashSaleList() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params =
                    GetFlashSaleListForSellerUseCase.Param( "upcoming", 0)
                val response = getFlashSaleListForSellerUseCase.execute(params)
            },
            onError = { error ->
            }
        )

    }
}