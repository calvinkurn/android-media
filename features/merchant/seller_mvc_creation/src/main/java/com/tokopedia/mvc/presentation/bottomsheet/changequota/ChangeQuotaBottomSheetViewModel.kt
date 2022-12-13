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
import com.tokopedia.mvc.presentation.bottomsheet.changequota.mapper.VoucherToChangeQuotaUiModel.toChangeQuotaUiModel
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.ChangeQuotaModel
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.ChangeQuotaUiEffect
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaState
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaState.SuccessToGetDetailVoucher
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
        private const val APPLY_ALL_PERIOD_COUPON = 1
        private const val APPLY_ONLY_THIS_PERIOD_COUPON = 0
        private const val MAX_QUOTA = 999L
        private const val MIN_QUOTA_FOR_NOT_STARTED_CAMPAIGN = 1
        private const val RESTART_DATA_ACTIVITY = -1
    }

    private val _inputQuotaValidation = MutableStateFlow(ChangeQuotaUiEffect())
    val inputQuotaValidation = _inputQuotaValidation.asStateFlow()

    private var _changeQuotaUiModel = MutableLiveData<UpdateQuotaState>()
    val changeQuotaUiModel: LiveData<UpdateQuotaState>
        get() = _changeQuotaUiModel
    private var changeQuotaModel: ChangeQuotaModel = ChangeQuotaModel()

    fun getVoucherDetail(id: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(id)
                val response = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _changeQuotaUiModel.postValue(SuccessToGetDetailVoucher(response.toChangeQuotaUiModel()))
                changeQuotaModel = response.toChangeQuotaUiModel()
                setOptionsApplyPeriodCoupon(APPLY_ONLY_THIS_PERIOD_COUPON)
            },
            onError = { error ->
                _changeQuotaUiModel.postValue(UpdateQuotaState.FailToGetDetailVoucher(error))
            }
        )
    }

    fun isValidInput(inputQuota: Long) {
        val minimumQuota = getMinimumQuotaOnVoucher()
        when {
            minimumQuota > inputQuota -> {
                _inputQuotaValidation.update {
                    val estimation =
                        calculateEstimation(changeQuotaModel.maxBenefit.orZero(), inputQuota)
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
                    calculateEstimation(changeQuotaModel.maxBenefit.orZero(), inputQuota)
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
                    calculateEstimation(changeQuotaModel.maxBenefit.orZero(), inputQuota)
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
        return if (changeQuotaModel.voucherStatus == VoucherStatus.NOT_STARTED) {
            MIN_QUOTA_FOR_NOT_STARTED_CAMPAIGN.toLong()
        } else {
            changeQuotaModel.currentQuota
        }
    }

    fun setOptionsApplyPeriodCoupon(position: Int) {
        _inputQuotaValidation.update {
            it.copy(
                isSelectedOptions = position != RESTART_DATA_ACTIVITY
            )
        }
        changeQuotaModel =
            changeQuotaModel.copy(isApplyToAllPeriodCoupon = position == APPLY_ALL_PERIOD_COUPON)
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
                    changeQuotaModel.voucherType,
                    changeQuotaModel.isVoucherProduct
                )
                val metadataDeferred =
                    async { getInitiateVoucherPageUseCase.execute(metadataParam) }
                val token = metadataDeferred.await()
                val updateQuotaStatus = updateQuota.execute(
                    changeQuotaModel.voucherId,
                    quota,
                    changeQuotaModel.isApplyToAllPeriodCoupon,
                    token.token
                )
                if (updateQuotaStatus) {
                    _changeQuotaUiModel.postValue(
                        UpdateQuotaState.SuccessToUpdate(
                            changeQuotaModel.voucherName,
                            changeQuotaModel.isApplyToAllPeriodCoupon
                        )
                    )
                }
            },
            onError = { error ->
                _changeQuotaUiModel.postValue(
                    UpdateQuotaState.FailToUpdate(
                        changeQuotaModel.voucherName,
                        error
                    )
                )
            }
        )
    }

    fun restartVoucher() {
        changeQuotaModel = changeQuotaModel.copy(isApplyToAllPeriodCoupon = false)
        _changeQuotaUiModel.postValue(SuccessToGetDetailVoucher(changeQuotaModel))
        setOptionsApplyPeriodCoupon(
            if (isMultiPeriod()) RESTART_DATA_ACTIVITY else APPLY_ONLY_THIS_PERIOD_COUPON
        )
    }

    private fun isMultiPeriod(): Boolean {
        return changeQuotaModel.isMultiPeriod
    }
}
