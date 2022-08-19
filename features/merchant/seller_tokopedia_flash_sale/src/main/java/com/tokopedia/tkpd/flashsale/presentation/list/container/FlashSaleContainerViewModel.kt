package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FlashSaleContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase
) : BaseViewModel(dispatchers.main) {

    data class UiState(
        val isLoading: Boolean = true,
        val tabsMetadata: List<TabMetadata> = emptyList(),
        val targetTabPosition: Int = 0,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        object GetTabsMetadata : UiEvent()
    }

    sealed class UiEffect {
        data class ShowToaster(val throwable: Throwable) : UiEffect()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = this._uiEffect.asSharedFlow()


    fun processEvent(event : UiEvent) {
        when (event) {
            is UiEvent.GetTabsMetadata -> {
                _uiState.update { it.copy(isLoading = true) }
                getTabsMetaData()
            }
        }
    }

    private fun getTabsMetaData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val tabs = getFlashSaleListForSellerMetaUseCase.execute()
                _uiState.update { it.copy(isLoading = false,  error = null, tabsMetadata = tabs) }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
                _uiEffect.emit(UiEffect.ShowToaster(error))
            }
        )

    }

}