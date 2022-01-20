package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductParams
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductResponse
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.CreateCouponProductUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponProductUseCase: CreateCouponProductUseCase
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = MutableLiveData<Result<CreateCouponProductResponse>>()
    val createCoupon: LiveData<Result<CreateCouponProductResponse>>
        get() = _createCoupon

    fun validateCoupon(couponSettings: CouponSettings? , couponInformation: CouponInformation?, couponProducts: List<CouponProduct>) {
        if (couponSettings == null) {
            _areInputValid.value = false
            return
        }

        if (couponInformation == null) {
            _areInputValid.value = false
            return
        }

        if (couponProducts.isEmpty()) {
            _areInputValid.value = false
            return
        }

        _areInputValid.value = true
    }


    fun createCoupon(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>,
        token : String
    ) {

        val isPublic = if (couponInformation.target == CouponInformation.Target.PUBLIC) 1 else 0
        val startDate = couponInformation.period.startDate.parseTo(DateTimeUtils.DASH_DATE_FORMAT)
        val startHour = couponInformation.period.startDate.parseTo(DateTimeUtils.HOUR_FORMAT)
        val endDate = couponInformation.period.endDate.parseTo(DateTimeUtils.DASH_DATE_FORMAT)
        val endHour = couponInformation.period.endDate.parseTo(DateTimeUtils.HOUR_FORMAT)

        val benefitType = when (couponSettings.discountType) {
            DiscountType.NONE -> ""
            DiscountType.NOMINAL -> "idr"
            DiscountType.PERCENTAGE -> "percent"
        }
        val couponType = when (couponSettings.type) {
            CouponType.NONE -> ""
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "shipping"
        }

        val params = CreateCouponProductParams(
            benefitIdr = couponSettings.discountAmount,
            benefitMax = couponSettings.maxDiscount,
            benefitPercent = couponSettings.discountPercentage,
            benefitType = benefitType,
            code = couponInformation.code,
            couponName = couponInformation.name,
            couponType = couponType,
            dateStart = startDate,
            dateEnd = endDate,
            hourStart = startHour,
            hourEnd = endHour,
            image = "819f5677-e6c7-49b9-872c-fe994c94dc9",
            imageSquare = "819f5677-e6c7-49b9-872c-fe994c94dc9b",
            imagePortrait = "819f5677-e6c7-49b9-872c-fe994c94dc9b",
            isPublic = isPublic,
            minPurchase = couponSettings.minimumPurchase,
            quota = couponSettings.quota,
            token = token,
            source = VoucherSource.SELLERAPP,
            targetBuyer = 0,
            minimumTierLevel = 0,
            isLockToProduct = 1,
            productIds = couponProducts.joinToString(separator = ","){ it.id.toString() },
            productIdsCsvUrl = ""
        )

        val json = Gson().toJson(params)
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) { createCouponProductUseCase.execute(params) }
                _createCoupon.value = Success(result)
            },
            onError = {
                _createCoupon.value = Fail(it)
            }
        )
    }
}