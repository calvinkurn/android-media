package com.tokopedia.mvc.presentation.creation.step3

import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherSettingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel(dispatchers.main) {

    private val _uiState = MutableStateFlow(VoucherCreationStepThreeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<VoucherCreationStepThreeAction>(replay = 1)
    val uiAction = _uiAction.asSharedFlow()

    private val currentState: VoucherCreationStepThreeUiState
        get() = _uiState.value

    fun processEvent(event: VoucherCreationStepThreeEvent) {
        when (event) {
            is VoucherCreationStepThreeEvent.InitVoucherConfiguration -> {
                initVoucherConfiguration(event.voucherConfiguration)
            }
            is VoucherCreationStepThreeEvent.ChooseBenefitType -> {}
            is VoucherCreationStepThreeEvent.ChoosePromoType -> {}
            is VoucherCreationStepThreeEvent.ChooseTargetBuyer -> {}
            is VoucherCreationStepThreeEvent.HandleCoachMark -> {}
            is VoucherCreationStepThreeEvent.NavigateToNextStep -> {}
            is VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged -> {}
            is VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged -> {}
            is VoucherCreationStepThreeEvent.OnInputNominalChanged -> {}
            is VoucherCreationStepThreeEvent.OnInputPercentageChanged -> {}
            is VoucherCreationStepThreeEvent.OnInputQuotaChanged -> {}
            is VoucherCreationStepThreeEvent.TapBackButton -> handleBackToPreviousStep()
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
        _uiAction.tryEmit(VoucherCreationStepThreeAction.BackToPreviousStep(currentState.voucherConfiguration))
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
                        isNominalError = validationResult.validationError.benefitIdr.isNotBlank(),
                        nominalErrorMsg = validationResult.validationError.benefitIdr,
                        isPercentageError = validationResult.validationError.benefitPercent.isNotBlank(),
                        percentageErrorMsg = validationResult.validationError.benefitPercent,
                        isMaxDeductionError = validationResult.validationError.benefitMax.isNotBlank(),
                        maxDeductionErrorMsg = validationResult.validationError.benefitMax,
                        isMinimumBuyError = validationResult.validationError.minPurchase.isNotBlank(),
                        minimumBuyErrorMsg = validationResult.validationError.minPurchase,
                        isQuotaError = validationResult.validationError.quota.isNotBlank(),
                        quotaErrorMsg = validationResult.validationError.quota
                    )
                }
            },
            onError = { }
        )
    }
}
