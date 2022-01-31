package com.tokopedia.vouchercreation.product.create.data.mapper

import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.response.MerchantVoucherModel
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import java.util.*
import javax.inject.Inject

class CouponDetailMapper @Inject constructor(){

    fun map(input: MerchantVoucherModel) : Coupon {
        val isPublic = if (input.isPublic == 1) CouponInformation.Target.PUBLIC else CouponInformation.Target.SPECIAL
        val startDate = input.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        val endDate = input.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        val information = CouponInformation(isPublic, input.voucherName, input.voucherCode, CouponInformation.Period(startDate, endDate))
        val settings = CouponSettings(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            input.discountAmt,
            0,
            input.discountAmtMax,
            input.voucherQuota,
            input.voucherMinimumAmt,
            (input.confirmedQuota * input.discountAmtMax).toLong()
        )

        val products = input.productIds.map {
            CouponProduct(it.parentProductId.toString(), 0,0.0f, "",0)
        }
        return Coupon(input.voucherId.toLong(), information, settings, products)
    }
}