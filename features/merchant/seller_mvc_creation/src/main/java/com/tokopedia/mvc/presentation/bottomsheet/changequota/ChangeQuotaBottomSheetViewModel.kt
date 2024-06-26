package com.tokopedia.mvc.presentation.bottomsheet.changequota

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.UpdateQuotaUseCase
import com.tokopedia.mvc.presentation.bottomsheet.changequota.mapper.ModelMapper.toUpdateQuotaModelMapper
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaModel
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaUiState
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaEffect
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaEffect.SuccessToGetDetailVoucher
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.APPLY_ALL_PERIOD_COUPON
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.APPLY_ONLY_PERIOD_COUPON
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.NOT_YET_APPLY_PERIOD_COUPON
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ChangeQuotaBottomSheetViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val updateQuota: UpdateQuotaUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val MAX_QUOTA = 999L
        private const val MIN_QUOTA_FOR_NOT_STARTED_CAMPAIGN = 1
    }

    private val _inputQuotaValidation = MutableStateFlow(UpdateQuotaUiState())
    val inputQuotaValidation = _inputQuotaValidation.asStateFlow()

    private var _changeQuotaUiModel = MutableLiveData<UpdateQuotaEffect>()
    val changeQuotaUiModel: LiveData<UpdateQuotaEffect>
        get() = _changeQuotaUiModel
    private var updateQuotaModel: UpdateQuotaModel = UpdateQuotaModel()

    fun getVoucherDetail(id: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(id)
                val response = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _changeQuotaUiModel.postValue(SuccessToGetDetailVoucher(response.toUpdateQuotaModelMapper()))
                updateQuotaModel = response.toUpdateQuotaModelMapper()

                // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
                //setOptionsApplyPeriodCoupon(isNeedToDisableYesButton())
            },
            onError = { error ->
                _changeQuotaUiModel.postValue(UpdateQuotaEffect.FailToGetDetailVoucher(error))
            }
        )
    }

    fun isValidInput(inputQuota: Long) {
        val minimumQuota = getMinimumQuotaOnVoucher()
        when {
            minimumQuota > inputQuota -> {
                _inputQuotaValidation.update {
                    val estimation =
                        calculateEstimation(updateQuotaModel.maxBenefit.orZero(), inputQuota)
                    it.copy(
                        isValidInput = false,
                        isInputOnUnderMinimumReq = true,
                        estimationSpending = estimation,
                        quotaReq = minimumQuota
                    )
                }
            }
            inputQuota > MAX_QUOTA -> {
                val estimation =
                    calculateEstimation(updateQuotaModel.maxBenefit.orZero(), inputQuota)
                _inputQuotaValidation.update {
                    it.copy(
                        isValidInput = false,
                        isInputOnUnderMinimumReq = false,
                        estimationSpending = estimation,
                        quotaReq = MAX_QUOTA
                    )
                }
            }
            else -> {
                val estimation =
                    calculateEstimation(updateQuotaModel.maxBenefit.orZero(), inputQuota)
                _inputQuotaValidation.update {
                    it.copy(
                        isValidInput = true,
                        estimationSpending = estimation,
                    )
                }
            }
        }
    }

    private fun getMinimumQuotaOnVoucher(): Long {
        return if (updateQuotaModel.voucherStatus == VoucherStatus.NOT_STARTED) {
            MIN_QUOTA_FOR_NOT_STARTED_CAMPAIGN.toLong()
        } else {
            updateQuotaModel.currentQuota
        }
    }

    fun calculateEstimation(maxBenefit: Long, quotaInput: Long): Long {
        return maxBenefit * quotaInput
    }

    fun changeQuota(quota: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadataParam = GetInitiateVoucherPageUseCase.Param(
                    VoucherAction.UPDATE,
                    updateQuotaModel.voucherType,
                    updateQuotaModel.isVoucherProduct
                )
                val metadataDeferred =
                    async { getInitiateVoucherPageUseCase.execute(metadataParam) }
                val token = metadataDeferred.await()
                val updateQuotaStatus = updateQuota.execute(
                    updateQuotaModel.voucherId,
                    quota,
                    updateQuotaModel.isApplyToAllPeriodCoupon,
                    token.token
                )
                if (updateQuotaStatus) {
                    _changeQuotaUiModel.postValue(
                        UpdateQuotaEffect.SuccessToUpdate(
                            updateQuotaModel.voucherName,
                            updateQuotaModel.isApplyToAllPeriodCoupon
                        )
                    )
                }
            },
            onError = { error ->
                _changeQuotaUiModel.postValue(
                    UpdateQuotaEffect.FailToUpdate(
                        updateQuotaModel.voucherName,
                        error
                    )
                )
            }
        )
    }

    fun getVoucherStatus() = updateQuotaModel.voucherStatus.name

    fun restartVoucher() {
        updateQuotaModel = updateQuotaModel.copy(isApplyToAllPeriodCoupon = false)
        _changeQuotaUiModel.postValue(SuccessToGetDetailVoucher(updateQuotaModel))

        // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
        /*setOptionsApplyPeriodCoupon(
            isNeedToDisableYesButton()
        )*/
    }

    // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
    /*
     private fun setOptionsApplyPeriodCoupon(optionsPosition: Int) {
        _inputQuotaValidation.update {
            it.copy(
                isSelectedOptions = optionsPosition != NOT_YET_APPLY_PERIOD_COUPON
            )
        }
        updateQuotaModel =
            updateQuotaModel.copy(isApplyToAllPeriodCoupon = optionsPosition == APPLY_ALL_PERIOD_COUPON)
    }

    private fun isNeedToDisableYesButton() =
        if (updateQuotaModel.isMultiPeriod) NOT_YET_APPLY_PERIOD_COUPON else APPLY_ONLY_PERIOD_COUPON*/
}
