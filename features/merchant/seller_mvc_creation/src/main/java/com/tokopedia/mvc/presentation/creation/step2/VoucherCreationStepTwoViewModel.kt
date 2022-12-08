package com.tokopedia.mvc.presentation.creation.step2

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.presentation.creation.step2.helper.ErrorHelper
import com.tokopedia.mvc.presentation.creation.step2.helper.ErrorMessageHelper
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherCreationStepTwoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper
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

    init {
        handleVoucherInputValidation()
    }

    fun processEvent(event: VoucherCreationStepTwoEvent) {
        when (event) {
            is VoucherCreationStepTwoEvent.InitVoucherConfiguration -> initVoucherConfiguration(
                event.voucherConfiguration
            )
            is VoucherCreationStepTwoEvent.ChooseVoucherTarget -> handleVoucherTargetSelection(event.isPublic)
            is VoucherCreationStepTwoEvent.TapBackButton -> handleBackToPreviousStep(currentState.voucherConfiguration)
            is VoucherCreationStepTwoEvent.OnVoucherNameChanged -> handleVoucherNameChanges(event.voucherName)
            is VoucherCreationStepTwoEvent.OnVoucherCodeChanged -> handleVoucherCodeChanges(event.voucherCode)
            is VoucherCreationStepTwoEvent.ValidateVoucherInput -> {}
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
                    isVoucherPublic = isPublic,
                    voucherCode = ""
                ),
                isVoucherCodeError = false,
                voucherCodeErrorMsg = ""
            )
        }
    }

    private fun handleVoucherNameChanges(voucherName: String) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(voucherName = voucherName)
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleVoucherCodeChanges(voucherCode: String) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(voucherCode = voucherCode)
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleVoucherInputValidation() {
        val voucherConfiguration = currentState.voucherConfiguration
        _uiState.update {
            it.copy(
                isVoucherNameError = ErrorHelper.getVoucherInputErrorStatus(voucherConfiguration.voucherName),
                voucherNameErrorMsg = errorMessageHelper.getVoucherInputErrorMessage(voucherConfiguration.voucherName),
                isVoucherCodeError = ErrorHelper.getVoucherInputErrorStatus(voucherConfiguration.voucherCode),
                voucherCodeErrorMsg = errorMessageHelper.getVoucherInputErrorMessage(voucherConfiguration.voucherCode)
            )
        }
    }
}
