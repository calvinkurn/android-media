package com.tokopedia.vouchercreation.common.mapper

import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import javax.inject.Inject

class CouponMapper @Inject constructor() {

    companion object {
        private const val DISCOUNT_TYPE_NOMINAL = "idr"
        private const val EMPTY_STRING = ""
    }

    fun map(coupon : CouponUiModel): Coupon {

        val startDate = coupon.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        val endDate = coupon.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)

        val isPublic = if (coupon.isPublic) {
            CouponInformation.Target.PUBLIC
        } else {
            CouponInformation.Target.PRIVATE
        }

        val information = CouponInformation(
            isPublic,
            coupon.name,
            coupon.code,
            CouponInformation.Period(startDate, endDate)
        )

        val maxExpense = (coupon.quota * coupon.discountAmtMax).toLong()
        val discountPercentage = if (coupon.discountTypeFormatted == DISCOUNT_TYPE_NOMINAL) {
            coupon.discountAmt
        } else {
            NumberConstant.PERCENT
        }

        val couponType = if (coupon.type == VoucherTypeConst.FREE_ONGKIR) {
            CouponType.FREE_SHIPPING
        } else {
            CouponType.CASHBACK
        }

        val discountType = if (coupon.discountTypeFormatted == DISCOUNT_TYPE_NOMINAL) {
            DiscountType.NOMINAL
        } else {
            DiscountType.PERCENTAGE
        }

        val setting = CouponSettings(
            couponType,
            discountType,
            MinimumPurchaseType.NOMINAL,
            coupon.discountAmt,
            discountPercentage,
            coupon.discountAmtMax,
            coupon.quota,
            coupon.minimumAmt,
            maxExpense
        )

        val products = coupon.productIds.map { productId -> CouponProduct(productId.toString(), EMPTY_STRING, NumberConstant.ZERO) }

        return Coupon(coupon.id.toLong(), information, setting, products)
    }

}