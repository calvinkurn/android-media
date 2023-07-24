package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.TickerConstant
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductSubmissionProgressUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSellerStatusUseCase
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
    private val getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase
) : BaseViewModel(dispatchers.main) {

    data class UiState(
        val isLoading: Boolean = true,
        val tickerMessage: String = "",
        val tabs: List<TabMetadata.Tab> = emptyList(),
        val targetTabPosition: Int = 0,
        val showTicker: Boolean = false,
        val error: Throwable? = null,
        val isEligibleUsingFeature: Boolean = true,
        val tickerList: List<RemoteTicker> = emptyList()
    )

    sealed class UiEvent {
        object GetPrerequisiteData : UiEvent()
    }

    sealed class UiEffect {
        data class ErrorFetchTabsMetaData(val throwable: Throwable) : UiEffect()
        object ShowIneligibleAccessWarning : UiEffect()
        data class FlashSaleSubmissionProgress(val uiModel: List<FlashSaleProductSubmissionProgress.Campaign>) : UiEffect()
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<UiEffect>(replay = 1)
    val uiEffect = this._uiEffect.asSharedFlow()

    fun processEvent(event: UiEvent, rollenceValueList: List<String>) {
        when (event) {
            is UiEvent.GetPrerequisiteData -> {
                _uiState.update { it.copy(isLoading = true, error = null) }
                getPrerequisiteData(rollenceValueList)
            }
        }
    }

    private fun getPrerequisiteData(rollenceValueList: List<String>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val sellerEligibilityDeferred = async { getFlashSaleSellerStatusUseCase.execute() }
                val tabMetadataDeferred = async { getFlashSaleListForSellerMetaUseCase.execute() }

                val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
                    GetTargetedTickerUseCase.Param.Target(
                        type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                        values = rollenceValueList
                    )
                )
                val tickerParams = GetTargetedTickerUseCase.Param(
                    page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
                    targets = targetParams
                )
                val tickersDeffered = async { getTargetedTickerUseCase.execute(tickerParams) }
                val tickers = tickersDeffered.await()

                val sellerEligibility = sellerEligibilityDeferred.await()
                val tabMetadata = tabMetadataDeferred.await()

                val isEligibleUsingFeature = sellerEligibility.isEligibleUsingFeature()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        tabs = tabMetadata.tabs,
                        showTicker = tickers.isNotEmpty(),
                        tickerList = tickers,
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

    fun getFlashSaleSubmissionProgress() {
        launchCatchError(
            dispatchers.io,
            block = {
                val flashSaleSubmissionProgress = getFlashSaleProductSubmissionProgressResponse()
                _uiEffect.emit(UiEffect.FlashSaleSubmissionProgress(flashSaleSubmissionProgress.listCampaign))
            }
        ) { }
    }

    private suspend fun getFlashSaleProductSubmissionProgressResponse(): FlashSaleProductSubmissionProgress {
        return getFlashSaleProductSubmissionProgressUseCase.execute(
            GetFlashSaleProductSubmissionProgressUseCase.Param(checkProgress = true)
        )
    }
}
