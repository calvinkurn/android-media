package com.tokopedia.vouchercreation.product.update.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class UpdateCouponPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase,
    private val getCouponDetailUseCase: GetCouponFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _updateCouponResult = SingleLiveEvent<Result<Boolean>>()
    val updateCouponResult: LiveData<Result<Boolean>>
        get() = _updateCouponResult

    private val _couponDetail = MutableLiveData<Result<Coupon>>()
    val couponDetail: LiveData<Result<Coupon>>
        get() = _couponDetail

    private val _startDate = SingleLiveEvent<Date>()
    val startDate: LiveData<Date>
        get() = _startDate

    private val _endDate = SingleLiveEvent<Date>()
    val endDate: LiveData<Date>
        get() = _endDate

    private var currentlySelectedStartDate = Date()
    private var currentlySelectedEndDate = Date()
    private var coupon : Coupon? = null


    fun setCouponData(coupon : Coupon) {
        this.coupon = coupon
    }

    fun setCurrentlySelectedStartDate(startDate: Date) {
        currentlySelectedStartDate = startDate
    }

    fun setCurrentlySelectedEndDate(endDate: Date) {
        currentlySelectedEndDate = endDate
    }

    fun openStartDateTimePicker() {
        _startDate.value = currentlySelectedStartDate
    }

    fun openEndDateTimePicker() {
        _endDate.value = currentlySelectedEndDate
    }

    fun updateCoupon() {
        val newPeriod = CouponInformation.Period(currentlySelectedStartDate, currentlySelectedEndDate)

        val coupon = coupon ?: return
        val updatedCouponInformation = coupon.information.copy(
            target = coupon.information.target,
            name = coupon.information.name,
            code = coupon.information.code,
            period = newPeriod
        )

        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    updateCouponUseCase.execute(
                        this,
                        ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                        coupon.id,
                        updatedCouponInformation,
                        coupon.settings,
                        coupon.products
                    )
                }
                _updateCouponResult.value = Success(result)
            },
            onError = {
                _updateCouponResult.value = Fail(it)
            }
        )
    }

    fun getCouponDetail(couponId: Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.execute(this, couponId)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }
}