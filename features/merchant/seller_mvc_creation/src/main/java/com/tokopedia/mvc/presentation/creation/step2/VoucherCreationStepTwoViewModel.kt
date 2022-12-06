package com.tokopedia.mvc.presentation.creation.step2

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherCreationStepTwoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val TRUE = 1
    }

    private val _uiState = MutableStateFlow(VoucherCreationStepTwoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<VoucherCreationStepTwoAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    private val currentState: VoucherCreationStepTwoUiState
        get() = _uiState.value

    fun processEvent(event: VoucherCreationStepTwoEvent) {
        when (event) {
            is VoucherCreationStepTwoEvent.InitVoucherConfiguration -> initVoucherConfiguration(
                event.voucherConfiguration
            )
            is VoucherCreationStepTwoEvent.ChooseVoucherTarget -> handleVoucherTargetSelection(event.isPublic)
            is VoucherCreationStepTwoEvent.TapBackButton -> handleBackToPreviousStep(currentState.voucherConfiguration)
            is VoucherCreationStepTwoEvent.ValidateVoucherNameInput -> handleVoucherNameValidation(
                event.voucherName
            )
        }
    }

    private fun initVoucherConfiguration(voucherConfiguration: VoucherConfiguration) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = voucherConfiguration
            )
        }
    }

    private fun handleBackToPreviousStep(voucherConfiguration: VoucherConfiguration) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = voucherConfiguration
            )
        }
        _uiAction.tryEmit(VoucherCreationStepTwoAction.BackToPreviousStep(currentState.voucherConfiguration))
    }

    private fun handleVoucherTargetSelection(isPublic: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    isVoucherPublic = isPublic
                )
            )
        }
    }

    private fun handleVoucherNameValidation(voucherName: String) {
        when {
            voucherName.count() < 5 -> {
                _uiState.update {
                    it.copy(
                        isVoucherNameError = true,
                        voucherNameErrorMsg = "Minimal 5 karakter."
                    )
                }
            }
            voucherName.isEmpty() -> {
                _uiState.update {
                    it.copy(
                        isVoucherNameError = true,
                        voucherNameErrorMsg = "Kamu belum mengisi informasi ini."
                    )
                }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        isVoucherNameError = false,
                        voucherNameErrorMsg = "",
                        voucherConfiguration = it.voucherConfiguration.copy(voucherName = voucherName)
                    )
                }
            }
        }
    }
}
