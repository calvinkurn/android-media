package com.tokopedia.mvc.presentation.creation.step3

import android.content.SharedPreferences
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepThreeFieldValidation
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.mvc.util.constant.CommonConstant
import com.tokopedia.mvc.util.constant.TickerConstant
import com.tokopedia.mvc.util.extension.firstTickerMessage
import com.tokopedia.mvc.util.extension.isCashback
import com.tokopedia.mvc.util.extension.isDiscount
import com.tokopedia.mvc.util.extension.isFreeShipping
import com.tokopedia.mvc.util.extension.isPrivate
import com.tokopedia.mvc.util.extension.isProductVoucher
import com.tokopedia.mvc.util.extension.isPublic
import com.tokopedia.mvc.util.extension.isShopVoucher
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class VoucherSettingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase,
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
                initVoucherConfiguration(event.pageMode, event.voucherConfiguration)
            }
            is VoucherCreationStepThreeEvent.ChoosePromoType -> handlePromoTypeSelection(event.promoType)
            is VoucherCreationStepThreeEvent.ChooseBenefitType -> handleBenefitTypeSelection(event.benefitType)
            is VoucherCreationStepThreeEvent.OnInputNominalChanged -> handleNominalInputChanges(event.nominal)
            is VoucherCreationStepThreeEvent.OnInputPercentageChanged -> handlePercentageInputChanges(event.percentage)
            is VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged -> handleMaxDeductionInputChanges(event.maxDeduction)
            is VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged -> handleMinimumBuyInputChanges(event.minimumBuy)
            is VoucherCreationStepThreeEvent.OnInputQuotaChanged -> handleQuotaInputChanges(event.quota)
            is VoucherCreationStepThreeEvent.ChooseTargetBuyer -> handleTargetBuyerSelection(event.targetBuyer)
            is VoucherCreationStepThreeEvent.HandleCoachMark -> handleCoachmark()
            is VoucherCreationStepThreeEvent.TapBackButton -> handleBackToPreviousStep()
            is VoucherCreationStepThreeEvent.NavigateToNextStep -> handleNavigateToNextStep()
            is VoucherCreationStepThreeEvent.ResetInput -> handleInputReset()
        }
    }

    private fun initVoucherConfiguration(
        pageMode: PageMode,
        voucherConfiguration: VoucherConfiguration
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val action = if (pageMode == PageMode.CREATE) VoucherAction.CREATE else VoucherAction.UPDATE
                val voucherCreationMetadataParam = GetInitiateVoucherPageUseCase.Param(
                    action = action,
                    promoType = voucherConfiguration.promoType,
                    isVoucherProduct = voucherConfiguration.isVoucherProduct
                )

                val voucherCreationMetadata = getInitiateVoucherPageUseCase.execute(voucherCreationMetadataParam)
                val isDiscountPromoTypeEnabled = voucherCreationMetadata.discountActive

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pageMode = pageMode,
                        voucherConfiguration = voucherConfiguration.copy(
                            isFinishFilledStepTwo = true
                        ),
                        isDiscountPromoTypeEnabled = isDiscountPromoTypeEnabled
                    )
                }

                getTickerWording()
                handleVoucherInputValidation()
            },
            onError = {}
        )
    }

    private fun getTickerWording() {
        launchCatchError(
            dispatchers.io,
            block = {
                val tickerWordingParam = GetTargetedTickerUseCase.Param(TickerConstant.REMOTE_TICKER_KEY_VOUCHER_CREATION_PAGE)
                val tickerWordings = getTargetedTickerUseCase.execute(tickerWordingParam)
                val tickerWording = tickerWordings.getTargetedTicker.list.firstTickerMessage()

                _uiState.update {
                    it.copy(discountPromoTypeDisabledReason = tickerWording)
                }
            },
            onError = {}
        )
    }

    fun getCurrentVoucherConfiguration(): VoucherConfiguration {
        return currentState.voucherConfiguration
    }

    private fun handleBackToPreviousStep() {
        _uiAction.tryEmit(VoucherCreationStepThreeAction.BackToPreviousStep(currentState.voucherConfiguration))
    }

    private fun handleNavigateToNextStep() {
        _uiAction.tryEmit(VoucherCreationStepThreeAction.ContinueToNextStep(currentState.voucherConfiguration))
    }

    private fun handlePromoTypeSelection(promoType: PromoType) {
        val isDiscountPromoTypeEnabled = currentState.isDiscountPromoTypeEnabled
        val isAllowedToChangePromoType = isAllowedToChangePromoType(promoType, isDiscountPromoTypeEnabled)

        if (isAllowedToChangePromoType) {
            val voucherServiceType =
                getVoucherServiceType(currentState.voucherConfiguration.isVoucherProduct)
            val voucherTarget = getVoucherTarget(currentState.voucherConfiguration.isVoucherPublic)
            val availableTargetBuyer = findBuyerTarget(voucherServiceType, voucherTarget, promoType)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    voucherConfiguration = it.voucherConfiguration.copy(
                        promoType = promoType
                    ),
                    spendingEstimation = 0,
                    availableTargetBuyer = availableTargetBuyer
                )
            }
        }
        handleVoucherInputValidation()
    }

    private fun handleBenefitTypeSelection(benefitType: BenefitType) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    benefitType = benefitType
                )
            )
        }
        setSpendingEstimation()
    }

    private fun handleNominalInputChanges(nominal: Long) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    benefitIdr = nominal.orZero()
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepThreeFieldValidation.NOMINAL)
            )
        }
        handleVoucherInputValidation()
        setSpendingEstimation()
    }

    private fun handlePercentageInputChanges(percentage: Long) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    benefitPercent = percentage.toInt().orZero()
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepThreeFieldValidation.PERCENTAGE)
            )
        }
        handleVoucherInputValidation()
        setSpendingEstimation()
    }

    private fun handleMaxDeductionInputChanges(maxDeduction: Long) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    benefitMax = maxDeduction.orZero()
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepThreeFieldValidation.MAX_DEDUCTION)
            )
        }
        handleVoucherInputValidation()
        setSpendingEstimation()
    }

    private fun handleMinimumBuyInputChanges(minimumBuy: Long) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    minPurchase = minimumBuy.orZero()
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepThreeFieldValidation.MINIMUM_BUY)
            )
        }
        handleVoucherInputValidation()
    }

    private fun handleQuotaInputChanges(quota: Long) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    quota = quota.orZero()
                ),
                fieldValidated = getFieldValidated(VoucherCreationStepThreeFieldValidation.QUOTA)
            )
        }
        handleVoucherInputValidation()
        setSpendingEstimation()
    }

    private fun handleTargetBuyerSelection(targetBuyer: VoucherTargetBuyer) {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    targetBuyer = targetBuyer
                )
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

    private fun getVoucherServiceType(isVoucherProduct: Boolean): VoucherServiceType {
        return if (isVoucherProduct) {
            VoucherServiceType.PRODUCT_VOUCHER
        } else {
            VoucherServiceType.SHOP_VOUCHER
        }
    }

    private fun getVoucherTarget(isPublic: Boolean): VoucherTarget {
        return if (isPublic) {
            VoucherTarget.PUBLIC
        } else {
            VoucherTarget.PRIVATE
        }
    }

    private fun setSpendingEstimation() {
        val currentConfiguration = currentState.voucherConfiguration
        val spendingEstimation = when (currentConfiguration.promoType) {
            PromoType.FREE_SHIPPING -> {
                currentConfiguration.benefitIdr * currentConfiguration.quota
            }
            else -> {
                getSpendingEstimationBasedOnBenefitType(currentConfiguration)
            }
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                spendingEstimation = spendingEstimation
            )
        }
    }

    private fun handleInputReset() {
        _uiState.update {
            it.copy(
                isLoading = false,
                voucherConfiguration = it.voucherConfiguration.copy(
                    benefitIdr = 0,
                    benefitPercent = 0,
                    benefitMax = 0,
                    minPurchase = 0,
                    quota = 0
                )
            )
        }
    }

    private fun getSpendingEstimationBasedOnBenefitType(currentConfiguration: VoucherConfiguration): Long {
        return when (currentConfiguration.benefitType) {
            BenefitType.NOMINAL -> currentConfiguration.benefitIdr * currentConfiguration.quota
            else -> currentConfiguration.benefitMax * currentConfiguration.quota
        }
    }

    private fun handleCoachmark() {
        if (!isCoachMarkShown()) {
            _uiAction.tryEmit(VoucherCreationStepThreeAction.ShowCoachmark)
        }
    }

    private fun isCoachMarkShown(): Boolean {
        return sharedPreferences.getBoolean(
            CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_THREE_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(CommonConstant.SHARED_PREF_VOUCHER_CREATION_STEP_THREE_COACH_MARK, true)
            .apply()
    }

    private fun getFieldValidated(field: VoucherCreationStepThreeFieldValidation): VoucherCreationStepThreeFieldValidation {
        return if (currentState.pageMode == PageMode.CREATE) {
            field
        } else {
            VoucherCreationStepThreeFieldValidation.ALL
        }
    }

    private fun findBuyerTarget(
        voucherType: VoucherServiceType,
        voucherTarget: VoucherTarget,
        promoType: PromoType
    ): List<VoucherTargetBuyer> {
        return when {
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isCashback() -> listOf(
                VoucherTargetBuyer.ALL_BUYER,
                VoucherTargetBuyer.NEW_FOLLOWER
            )
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isFreeShipping() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isDiscount() -> listOf(
                VoucherTargetBuyer.ALL_BUYER,
                VoucherTargetBuyer.NEW_FOLLOWER
            )
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isCashback() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isFreeShipping() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isDiscount() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isCashback() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isFreeShipping() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isDiscount() -> listOf(
                VoucherTargetBuyer.ALL_BUYER,
                VoucherTargetBuyer.NEW_FOLLOWER
            )
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isCashback() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isFreeShipping() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isDiscount() -> listOf(
                VoucherTargetBuyer.ALL_BUYER
            )
            else -> emptyList()
        }
    }

    /**
     * If newly selected promo type is discount while it was toggled off by Backend,
     * user cannot select discount promo type.
     */
    private fun isAllowedToChangePromoType(
        newPromoType: PromoType,
        isDiscountPromoTypeEnabled: Boolean
    ): Boolean {
        if (newPromoType == PromoType.FREE_SHIPPING) {
            return true
        }

        if (newPromoType == PromoType.CASHBACK) {
            return true
        }

        if (newPromoType == PromoType.DISCOUNT && !isDiscountPromoTypeEnabled) {
            return false
        }

        return true
    }
}
