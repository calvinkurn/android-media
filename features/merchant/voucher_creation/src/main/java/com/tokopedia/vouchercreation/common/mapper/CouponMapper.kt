package com.tokopedia.vouchercreation.common.mapper

import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

class CouponMapper @Inject constructor() {

    companion object {
        private const val DISCOUNT_TYPE_NOMINAL = 1
    }

    fun map(voucher: VoucherUiModel): Coupon {

        val startDate = voucher.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        val endDate = voucher.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)

        val isPublic = if (voucher.isPublic) {
            CouponInformation.Target.PUBLIC
        } else {
            CouponInformation.Target.SPECIAL
        }

        val information = CouponInformation(
            isPublic,
            voucher.name,
            voucher.code,
            CouponInformation.Period(startDate, endDate)
        )

        val maxExpense = (voucher.quota * voucher.discountAmtMax).toLong()
        val discountPercentage = if (voucher.discountType == DISCOUNT_TYPE_NOMINAL) {
            voucher.discountAmt
        } else {
            NumberConstant.PERCENT
        }

        val couponType = if (voucher.type == VoucherTypeConst.FREE_ONGKIR) {
            CouponType.FREE_SHIPPING
        } else {
            CouponType.CASHBACK
        }

        val discountType = if (voucher.discountType == DISCOUNT_TYPE_NOMINAL) {
            DiscountType.NOMINAL
        } else {
            DiscountType.PERCENTAGE
        }

        val setting = CouponSettings(
            couponType,
            discountType,
            MinimumPurchaseType.NOMINAL,
            voucher.discountAmt,
            discountPercentage,
            voucher.discountAmtMax,
            voucher.quota,
            voucher.minimumAmt,
            maxExpense
        )

        return Coupon(voucher.id.toLong(), information, setting, emptyList())
    }

}