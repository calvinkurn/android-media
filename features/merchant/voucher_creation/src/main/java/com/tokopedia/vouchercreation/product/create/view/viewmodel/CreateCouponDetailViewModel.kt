package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.NonNullLiveData
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel
import com.tokopedia.vouchercreation.product.create.view.uimodel.convertToCouponTargetEnum
import com.tokopedia.vouchercreation.shop.create.domain.usecase.validation.PeriodValidationUseCase
import com.tokopedia.vouchercreation.shop.create.domain.usecase.validation.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.PeriodValidation
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.VoucherTargetValidation
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class CreateCouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val voucherTargetValidationUseCase: VoucherTargetValidationUseCase,
    private val periodValidationUseCase: PeriodValidationUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val HOUR_FORMAT = "HH:mm"
        private const val MIN_CHAR_COUPON_NAME = 5
        private const val MIN_CHAR_COUPON_CODE = 5
    }

    private val mCouponTargetList = MutableLiveData<List<CouponTargetUiModel>>()
    val couponTargetList: LiveData<List<CouponTargetUiModel>>
        get() = mCouponTargetList

    private val mSelectedCouponTarget = NonNullLiveData(CouponTargetEnum.PUBLIC)
    val selectedCouponTarget: LiveData<CouponTargetEnum>
        get() = mSelectedCouponTarget

    private val mStartDateCalendarLiveData = NonNullLiveData<Calendar>(GregorianCalendar())
    val startDateCalendarLiveData: LiveData<Calendar>
        get() = mStartDateCalendarLiveData

    private val mEndDateCalendarLiveData = NonNullLiveData<Calendar>(GregorianCalendar())
    val endDateCalendarLiveData: LiveData<Calendar>
        get() = mEndDateCalendarLiveData

    private val mCouponValidationResult = MutableLiveData<Result<VoucherTargetValidation>>()
    val couponValidationResult: LiveData<Result<VoucherTargetValidation>>
        get() = mCouponValidationResult

    private val mPeriodValidationLiveData = MutableLiveData<Result<PeriodValidation>>()
    val periodValidationLiveData: LiveData<Result<PeriodValidation>>
        get() = mPeriodValidationLiveData

    private val mAllInputValid = MediatorLiveData<Boolean>().apply {
        addSource(mCouponValidationResult) {
            value = validateAllInput()
        }
        addSource(mPeriodValidationLiveData) {
            value = validateAllInput()
        }
    }
    val allInputValid: LiveData<Boolean>
        get() = mAllInputValid

    private fun validateAllInput(): Boolean {
        val couponValidation = mCouponValidationResult.value
        val periodValidation = mPeriodValidationLiveData.value
        return if (couponValidation is Success && periodValidation is Success) {
            !couponValidation.data.checkHasError() && !periodValidation.data.getIsHaveError()
        } else false
    }

    fun populateCouponTarget(selectedCoupon: CouponTargetEnum? = null) {
        mCouponTargetList.value = listOf(
            CouponTargetUiModel(
                R.drawable.ic_im_umum,
                R.string.mvc_create_target_public,
                R.string.mvc_create_coupon_target_public_desc,
                CouponTargetEnum.PUBLIC,
                selected = selectedCoupon == CouponTargetEnum.PUBLIC,
            ),
            CouponTargetUiModel(
                R.drawable.ic_im_terbatas,
                R.string.mvc_create_target_private,
                R.string.mvc_create_coupon_target_private_desc,
                CouponTargetEnum.PRIVATE,
                selected = selectedCoupon == CouponTargetEnum.PRIVATE,
            ),
        )
    }

    fun setSelectedCouponTarget(couponTarget: CouponTargetEnum) {
        mSelectedCouponTarget.value = couponTarget
    }

    fun setStartDateCalendar(startDate: Calendar) {
        mStartDateCalendarLiveData.value = startDate
    }

    fun setEndDateCalendar(endDate: Calendar) {
        mEndDateCalendarLiveData.value = endDate
    }

    fun validateCouponTarget(promoCode: String, couponName: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                voucherTargetValidationUseCase.params = VoucherTargetValidationUseCase
                    .createRequestParam(mSelectedCouponTarget.value.value, promoCode, couponName)
                voucherTargetValidationUseCase.executeOnBackground()
            }
            mCouponValidationResult.value = Success(result)
        }, onError = {
            mCouponValidationResult.value = Fail(it)
        })
    }

    fun validateCouponPeriod() {
        launchCatchError(
            block = {
                mPeriodValidationLiveData.value = Success(withContext(dispatchers.io) {
                    val dateStart = mStartDateCalendarLiveData.value.time.toFormattedString(DATE_FORMAT)
                    val hourStart = mStartDateCalendarLiveData.value.time.toFormattedString(HOUR_FORMAT)
                    val dateEnd = mEndDateCalendarLiveData.value.time.toFormattedString(DATE_FORMAT)
                    val hourEnd = mEndDateCalendarLiveData.value.time.toFormattedString(HOUR_FORMAT)
                    periodValidationUseCase.params = PeriodValidationUseCase
                        .createRequestParam(dateStart, dateEnd, hourStart, hourEnd)
                    periodValidationUseCase.executeOnBackground()
                })
            },
            onError = {
                mPeriodValidationLiveData.value = Fail(it)
            }
        )
    }

    fun validateMinCharCouponCode(couponCode: String) = couponCode.length > MIN_CHAR_COUPON_CODE

    fun validateMinCharCouponName(couponName: String) = couponName.length > MIN_CHAR_COUPON_NAME

    fun setCouponInformation(couponInformationData: CouponInformation) {
        couponInformationData.let {
            populateCouponTarget(it.target.convertToCouponTargetEnum())
            setSelectedCouponTarget(it.target.convertToCouponTargetEnum())
            setStartDateCalendar(GregorianCalendar().apply { time = it.period.startDate })
            setEndDateCalendar(GregorianCalendar().apply { time = it.period.endDate })
        }
    }
}