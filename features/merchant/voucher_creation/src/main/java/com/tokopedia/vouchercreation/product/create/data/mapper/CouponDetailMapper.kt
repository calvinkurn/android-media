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
       // return populateDummyCoupon()
    }

    private fun populateDummyCoupon(): Coupon {
        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 0, 28, 22, 30, 0) }
        val endDate = Calendar.getInstance().apply { set(2022, 0, 30, 22, 0, 0) }
        val period = CouponInformation.Period(startDate.time, endDate.time)

        val information = CouponInformation(
            CouponInformation.Target.PUBLIC,
            "Kupon Kopi Soe",
            "KOPSOE",
            period

        )

        val setting = CouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            10000,
            100,
            10000,
            10,
            5000,
            1000000
        )

        val products =
            listOf(
                CouponProduct(
                    "2147956088",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    19
                ),
                CouponProduct(
                    "15455652",
                    18000,
                    4.7F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    1000
                ),
                CouponProduct(
                    "15429644",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    2100
                ),
                CouponProduct(
                    "15409031",
                    25000,
                    4.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    31000
                )
            )

        return Coupon(9094, information, setting, products)
    }
}