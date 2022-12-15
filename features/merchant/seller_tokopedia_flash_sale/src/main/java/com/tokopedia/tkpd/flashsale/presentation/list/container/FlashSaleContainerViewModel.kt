package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSellerStatusUseCase
import com.tokopedia.tkpd.flashsale.util.preference.PreferenceDataStore
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FlashSaleContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase,
    private val getFlashSaleSellerStatusUseCase: GetFlashSaleSellerStatusUseCase,
    private val preferenceDataStore: PreferenceDataStore
) : BaseViewModel(dispatchers.main) {

    data class UiState(
        val isLoading: Boolean = true,
        val tickerMessage: String = "",
        val tabs: List<TabMetadata.Tab> = emptyList(),
        val targetTabPosition: Int = 0,
        val showTicker: Boolean = false,
        val error: Throwable? = null,
        val isEligibleUsingFeature: Boolean = true
    )

    sealed class UiEvent {
        object GetPrerequisiteData : UiEvent()
        object DismissMultiLocationTicker: UiEvent()
    }

    sealed class UiEffect {
        data class ErrorFetchTabsMetaData(val throwable: Throwable) : UiEffect()
        object ShowIneligibleAccessWarning : UiEffect()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = this._uiEffect.asSharedFlow()


    fun processEvent(event : UiEvent) {
        when (event) {
            is UiEvent.GetPrerequisiteData -> {
                _uiState.update { it.copy(isLoading = true, error = null) }
                getPrerequisiteData()
            }
            UiEvent.DismissMultiLocationTicker -> {
                preferenceDataStore.markMultiLocationTickerAsDismissed()
            }
        }
    }

    private fun getPrerequisiteData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val sellerEligibilityDeferred = async { getFlashSaleSellerStatusUseCase.execute() }
                val tabMetadataDeferred = async { getFlashSaleListForSellerMetaUseCase.execute() }

                val sellerEligibility = sellerEligibilityDeferred.await()
                val tabMetadata = tabMetadataDeferred.await()

                val isMultiLocationTickerPreviouslyDismissed = preferenceDataStore.isMultiLocationTickerDismissed()
                val showTicker = !isMultiLocationTickerPreviouslyDismissed

                val isEligibleUsingFeature = sellerEligibility.isEligibleUsingFeature()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        tabs = tabMetadata.tabs,
                        tickerMessage = tabMetadata.tickerNonMultiLocationMessage,
                        showTicker = showTicker,
                        isEligibleUsingFeature = isEligibleUsingFeature
                    )
                }

                if (!isEligibleUsingFeature) {
                    _uiEffect.emit(UiEffect.ShowIneligibleAccessWarning)
                }

            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
                _uiEffect.emit(UiEffect.ErrorFetchTabsMetaData(error))
            }
        )

    }

    private fun SellerEligibility.isEligibleUsingFeature(): Boolean {
        val isRbacRuleActive = isDeviceAllowed

        return when {
            isRbacRuleActive && isUserAllowed -> true
            !isRbacRuleActive -> true
            else -> false
        }
    }

}
