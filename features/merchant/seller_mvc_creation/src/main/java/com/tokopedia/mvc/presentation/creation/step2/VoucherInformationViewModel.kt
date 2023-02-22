package com.tokopedia.mvc.presentation.creation.step2

import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepTwoFieldValidation
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.mvc.util.constant.CommonConstant
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class VoucherInformationViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(VoucherCreationStepTwoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<VoucherCreationStepTwoAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    private val currentState: VoucherCreationStepTwoUiState
        get() = _uiState.value

    companion object {
        private const val ROUNDED_PERIOD_IN_MINUTES = 30
        private const val ROUNDED_PERIOD_HAPPEN_PER_DAY = 4
    }

    fun processEvent(event: VoucherCreationStepTwoEvent) {
        when (event) {
            is VoucherCreationStepTwoEvent.InitVoucherConfiguration -> initVoucherConfiguration(
                event.pageMode,
                event.voucherConfiguration
            )
            is VoucherCreationStepTwoEvent.ChooseVoucherTarget -> handleVoucherTargetSelection(
                event.isPublic,
                event.isChangingTargetBuyer
            )
            is VoucherCreationStepTwoEvent.TapBackButton -> handleBackToPreviousStep()
            is VoucherCreationStepTwoEvent.OnVoucherNameChanged -> handleVoucherNameChanges(event.voucherName)
            is VoucherCreationStepTwoEvent.OnVoucherCodeChanged -> handleVoucherCodeChanges(event.voucherCode)
            is VoucherCreationStepTwoEvent.OnVoucherRecurringToggled -> handleVoucherRecurringToggleChanges(
                event.isActive
            )
            is VoucherCreationStepTwoEvent.OnVoucherStartDateChanged -> setStartDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.OnVoucherEndDateChanged -> setEndDateTime(event.calendar)
            is VoucherCreationStepTwoEvent.OnVoucherRecurringPeriodSelected -> setRecurringPeriod(
                event.selectedRecurringPeriod
            )
            is VoucherCreationStepTwoEvent.NavigateToNextStep -> {
                handleNavigateToNextStep()
            }
            is VoucherCreationStepTwoEvent.HandleCoachMark -> {
                handleCoachmark()
            }
        }
    }

    private fun initVoucherConfiguration(
        pageMode: PageMode,
        voucherConfiguration: VoucherConfiguration
    ) {
        _uiState.update {
            it.copy(
                isLoading = false,
                pageMode = pageMode,
                voucherConfiguration = voucherConfiguration.copy(
                    isFinishFilledStepOne = true,
                    startPeriod = voucherConfiguration.startPeriod.roundTimePerHalfHour()
                )
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

    private fun handleVoucherTargetSelection(isPublic: Boolean, isChangingTargetBuyer: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    isVoucherPublic = isPublic,
                    targetBuyer = if (isChangingTargetBuyer) {
                        VoucherTargetBuyer.ALL_BUYER
                    } else {
                        it.voucherConfiguration.targetBuyer
                    }
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.VOUCHER_TARGET)
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleVoucherNameChanges(voucherName: String) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(voucherName = voucherName),
                fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.VOUCHER_NAME)
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleVoucherCodeChanges(voucherCode: String) {
        _uiState.update {
            it.copy(
                voucherConfiguration = it.voucherConfiguration.copy(voucherCode = voucherCode),
                fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.VOUCHER_CODE)
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
                    endHour = voucherConfiguration.endPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION),
                    quota = voucherConfiguration.quota
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
                voucherConfiguration = it.voucherConfiguration.copy(isPeriod = isActive),
                fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.ALL)
            )
        }
        handleVoucherInputValidation()
    }

    private fun setStartDateTime(startDate: Calendar?) {
        startDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(startPeriod = startDate.time),
                    fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.VOUCHER_START_DATE)
                )
            }
            handleVoucherInputValidation()
        }
    }

    private fun setEndDateTime(endDate: Calendar?) {
        endDate?.let {
            _uiState.update {
                it.copy(
                    voucherConfiguration = it.voucherConfiguration.copy(endPeriod = endDate.time),
                    fieldValidated = getFieldValidated(VoucherCreationStepTwoFieldValidation.VOUCHER_END_DATE)
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

    fun mapVoucherRecurringPeriodData(validationDate: List<VoucherValidationResult.ValidationDate>): List<DateStartEndData> {
        return validationDate.map {
            DateStartEndData(
                dateStart = it.dateStart,
                dateEnd = it.dateEnd,
                hourStart = it.hourStart,
                hourEnd = it.hourEnd
            )
        }
    }

    private fun handleCoachmark() {
        if (!isCoachMarkShown()) {
            _uiAction.tryEmit(VoucherCreationStepTwoAction.ShowCoachmark)
        }
    }

    private fun handleNavigateToNextStep() {
        _uiAction.tryEmit(
            VoucherCreationStepTwoAction.NavigateToNextStep(
                currentState.pageMode,
                currentState.voucherConfiguration
            )
        )
    }

    private fun isCoachMarkShown(): Boolean {
        return sharedPreferences.getBoolean(
            CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_TWO_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_TWO_COACH_MARK, true)
            .apply()
    }

    private fun getFieldValidated(field: VoucherCreationStepTwoFieldValidation): VoucherCreationStepTwoFieldValidation {
        return if (currentState.pageMode == PageMode.CREATE) {
            field
        } else {
            VoucherCreationStepTwoFieldValidation.ALL
        }
    }

    private fun Date.roundTimePerHalfHour(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = this

        val unroundedMinutes: Int = calendar.get(Calendar.MINUTE)
        val mod = unroundedMinutes % ROUNDED_PERIOD_IN_MINUTES
        calendar.add(
            Calendar.MINUTE,
            if (mod < ROUNDED_PERIOD_HAPPEN_PER_DAY) -mod else ROUNDED_PERIOD_IN_MINUTES - mod
        )
        return calendar.time
    }
}
