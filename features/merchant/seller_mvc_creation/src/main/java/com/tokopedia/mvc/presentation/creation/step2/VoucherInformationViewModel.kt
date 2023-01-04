package com.tokopedia.mvc.presentation.creation.step2

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
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
            is VoucherCreationStepTwoEvent.InitVoucherConfiguration -> initVoucherConfiguration(event.voucherConfiguration)
            is VoucherCreationStepTwoEvent.ChooseVoucherTarget -> handleVoucherTargetSelection(event.isPublic)
            is VoucherCreationStepTwoEvent.TapBackButton -> handleBackToPreviousStep()
            is VoucherCreationStepTwoEvent.OnVoucherNameChanged -> handleVoucherNameChanges(event.voucherName)
            is VoucherCreationStepTwoEvent.OnVoucherCodeChanged -> handleVoucherCodeChanges(event.voucherCode)
            is VoucherCreationStepTwoEvent.OnVoucherRecurringToggled -> handleVoucherRecurringToggleChanges(event.isActive)
            is VoucherCreationStepTwoEvent.OnVoucherStartDateChanged -> setStartDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.OnVoucherEndDateChanged -> setEndDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.OnVoucherRecurringPeriodSelected -> setRecurringPeriod(event.selectedRecurringPeriod)
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

    fun getCurrentVoucherConfiguration(): VoucherConfiguration {
        return currentState.voucherConfiguration
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
                    code = voucherConfiguration.voucherCode,
                    isPeriod = voucherConfiguration.isPeriod,
                    periodType = voucherConfiguration.periodType,
                    periodRepeat = voucherConfiguration.periodRepeat,
                    totalPeriod = voucherConfiguration.totalPeriod,
                    startDate = voucherConfiguration.startPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
                    endDate = voucherConfiguration.endPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
                    startHour = voucherConfiguration.startPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION),
                    endHour = voucherConfiguration.endPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION)
                )
                val validationResult =
                    voucherValidationPartialUseCase.execute(voucherValidationParam)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isVoucherNameError = validationResult.validationError.couponName.isNotBlank(),
                        voucherNameErrorMsg = validationResult.validationError.couponName,
                        isVoucherCodeError = validationResult.validationError.code.isNotBlank(),
                        voucherCodeErrorMsg = validationResult.validationError.code,
                        isStartDateError = validationResult.validationError.dateStart.isNotBlank(),
                        startDateErrorMsg = validationResult.validationError.dateStart,
                        isEndDateError = validationResult.validationError.dateEnd.isNotBlank(),
                        endDateErrorMsg = validationResult.validationError.dateEnd,
                        validationDate = validationResult.validationDate
                    )
                }
            },
            onError = { }
        )
    }

    private fun handleVoucherRecurringToggleChanges(isActive: Boolean) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(isPeriod = isActive)
            )
        }
        handleVoucherInputValidation()
    }

    private fun setStartDateTime(startDate: Calendar?) {
        startDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(startPeriod = startDate.time)
                )
            }
            handleVoucherInputValidation()
        }
    }

    private fun setEndDateTime(endDate: Calendar?) {
        endDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(endPeriod = endDate.time)
                )
            }
            handleVoucherInputValidation()
        }
    }

    private fun setRecurringPeriod(selectedRecurringPeriod: Int) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(totalPeriod = selectedRecurringPeriod)
            )
        }
        handleVoucherInputValidation()
    }
}
