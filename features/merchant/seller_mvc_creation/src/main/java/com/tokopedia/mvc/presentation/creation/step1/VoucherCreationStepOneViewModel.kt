package com.tokopedia.mvc.presentation.creation.step1

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherCreationStepOneViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val TRUE = 1
    }

    private val _uiState = MutableStateFlow(VoucherCreationStepOneUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<VoucherCreationStepOneAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    private val currentState: VoucherCreationStepOneUiState
        get() = _uiState.value

    fun processEvent(event: VoucherCreationEvent) {
        when (event) {
            is VoucherCreationEvent.ChooseVoucherType -> {
                handleVoucherTypeSelection(event.pageMode, event.isVoucherProduct)
            }
        }
    }

    private fun handleVoucherTypeSelection(
        pageMode: PageMode,
        isVoucherProduct: Boolean
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action =
                    if (pageMode == PageMode.CREATE) VoucherAction.CREATE else VoucherAction.UPDATE

                val metadataParam = GetInitiateVoucherPageUseCase.Param(
                    action = action,
                    promoType = currentState.voucherConfiguration.promoType,
                    isVoucherProduct = isVoucherProduct
                )
                val metadata = getInitiateVoucherPageUseCase.execute(metadataParam)

                if (metadata.isEligible == TRUE) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            voucherConfiguration = it.voucherConfiguration.copy(
                                isVoucherProduct = isVoucherProduct
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiAction.tryEmit(VoucherCreationStepOneAction.ShowIneligibleState(isVoucherProduct))
                }

            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
                _uiAction.tryEmit(VoucherCreationStepOneAction.ShowError(error))
            }
        )
    }
}
