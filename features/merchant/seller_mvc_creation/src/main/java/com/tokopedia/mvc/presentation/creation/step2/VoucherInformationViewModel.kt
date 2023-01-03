package com.tokopedia.mvc.presentation.creation.step2

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class VoucherInformationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase
) : BaseViewModel(dispatchers.main) {

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
            is VoucherCreationStepTwoEvent.TapBackButton -> handleBackToPreviousStep()
            is VoucherCreationStepTwoEvent.OnVoucherNameChanged -> handleVoucherNameChanges(event.voucherName)
            is VoucherCreationStepTwoEvent.OnVoucherCodeChanged -> handleVoucherCodeChanges(event.voucherCode)
            is VoucherCreationStepTwoEvent.OnVoucherStartDateChanged -> setStartDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.OnVoucherEndDateChanged -> setEndDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.ValidateVoucherInput -> {}
            is VoucherCreationStepTwoEvent.NavigateToNextStep -> {}
        }
    }

    private fun initVoucherConfiguration(voucherConfiguration: VoucherConfiguration) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = voucherConfiguration
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleBackToPreviousStep() {
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

        handleVoucherInputValidation()
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
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherValidationParam = VoucherValidationPartialUseCase.Param(
                    benefitIdr = voucherConfiguration.benefitIdr,
                    benefitMax = voucherConfiguration.benefitMax,
                    benefitPercent = voucherConfiguration.benefitPercent,
                    benefitType = voucherConfiguration.benefitType,
                    promoType = voucherConfiguration.promoType,
                    isVoucherProduct = voucherConfiguration.isVoucherProduct,
                    minPurchase = voucherConfiguration.minPurchase,
                    productIds = emptyList(),
                    targetBuyer = voucherConfiguration.targetBuyer,
                    couponName = voucherConfiguration.voucherName,
                    isPublic = voucherConfiguration.isVoucherPublic,
                    code = voucherConfiguration.voucherCode
                )
                val validationResult =
                    voucherValidationPartialUseCase.execute(voucherValidationParam).validationError
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isVoucherNameError = validationResult.couponName.isNotBlank(),
                        voucherNameErrorMsg = validationResult.couponName,
                        isVoucherCodeError = validationResult.code.isNotBlank(),
                        voucherCodeErrorMsg = validationResult.code
                    )
                }
            },
            onError = { }
        )
    }

    private fun setStartDateTime(startDate: Calendar?) {
        startDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(startPeriod = startDate.time)
                )
            }
        }
    }

    private fun setEndDateTime(endDate: Calendar?) {
        endDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(endPeriod = endDate.time)
                )
            }
        }
    }
}
