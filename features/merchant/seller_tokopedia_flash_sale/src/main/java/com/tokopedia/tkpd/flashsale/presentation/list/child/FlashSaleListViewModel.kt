package com.tokopedia.tkpd.flashsale.presentation.list.child

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FlashSaleListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(replay = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    data class UiState(
        val isLoading: Boolean = false,
        val flashSales: List<FlashSale> = emptyList(),
        val error: Throwable? = null
    )

    sealed class UiEvent {
        data class FetchError(val throwable: Throwable) : UiEvent()
    }

    fun getFlashSaleList(tabName : String, offset : Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = GetFlashSaleListForSellerUseCase.Param(tabName, offset)
                val campaigns = getFlashSaleListForSellerUseCase.execute(params)
                _uiState.update { it.copy(isLoading = false, flashSales = campaigns) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )

    }
}
