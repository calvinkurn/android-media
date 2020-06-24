package com.tokopedia.vouchercreation.common.domain.model

import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.CouponType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel

class UpdateVoucherParam (
        val voucher_id: Int = 0,
        val benefit_idr: Int = 0,
        val benefit_max: Int = 0,
        val benefit_percent: Int = 0,
        val benefit_type: String = "",
        val code: String = "",
        val coupon_name: String = "",
        val coupon_type: String = "",
        val date_start: String = "",
        val date_end: String = "",
        val hour_start: String = "",
        val hour_end: String = "",
        var image: String = "",
        var image_square: String = "",
        val is_public: Int = 0,
        val min_purchase: Int = 0,
        val quota: Int = 0,
        val token: String = "",
        val source: String = "") {

    companion object {

        @JvmStatic
        fun mapToParam(voucherReviewUiModel: VoucherReviewUiModel,
                       token: String,
                       voucherId: Int) : UpdateVoucherParam =
                voucherReviewUiModel.run {
                    var benefitPercent = 0
                    var benefitMax = 0
                    if (voucherType is VoucherImageType.Percentage) {
                        benefitPercent = (voucherType as? VoucherImageType.Percentage)?.percentage ?: 0
                        benefitMax = (voucherType as? VoucherImageType.Percentage)?.value ?: 0
                    }
                    UpdateVoucherParam(
                            voucher_id = voucherId,
                            benefit_idr = voucherType.value,
                            benefit_max = benefitMax,
                            benefit_percent = benefitPercent,
                            benefit_type = voucherType.benefitType,
                            code = promoCode,
                            coupon_name = voucherName,
                            coupon_type = voucherType.couponType,
                            date_start = startDate,
                            date_end = endDate,
                            hour_start = startHour,
                            hour_end = endHour,
                            image = "",
                            image_square = "",
                            is_public = targetType,
                            min_purchase = minPurchase,
                            quota = voucherQuota,
                            token = token,
                            source = VoucherSource.SELLERAPP
                    )
                }

        @JvmStatic
        fun mapToParam(voucherUiModel: VoucherUiModel,
                       token: String,
                       startDate: String,
                       startHour: String,
                       endDate: String,
                       endHour: String) : UpdateVoucherParam =
                with(voucherUiModel) {
                    UpdateVoucherParam(
                            voucher_id = id,
                            benefit_idr = discountAmt,
                            benefit_max = discountAmtMax,
                            benefit_percent = discountAmt,
                            benefit_type = discountTypeFormatted,
                            code = code,
                            coupon_name = name,
                            coupon_type = convertVoucherToCouponDefinition(type),
                            date_start = startDate,
                            date_end = endDate,
                            hour_start = startHour,
                            hour_end = endHour,
                            image = image,
                            image_square = imageSquare,
                            is_public = convertTargetType(isPublic),
                            min_purchase = minimumAmt,
                            quota = quota,
                            token = token,
                            source = VoucherSource.SELLERAPP
                    )
                }
    }

}

private fun convertVoucherToCouponDefinition(@VoucherTypeConst type: Int): String {
    return when(type) {
        VoucherTypeConst.FREE_ONGKIR -> CouponType.SHIPPING
        VoucherTypeConst.DISCOUNT -> CouponType.DISCOUNT
        VoucherTypeConst.CASHBACK -> CouponType.CASHBACK
        else -> CouponType.SHIPPING
    }
}

private fun convertTargetType(isPublic: Boolean): Int {
    return if (isPublic) {
        VoucherTargetType.PUBLIC
    } else {
        VoucherTargetType.PRIVATE
    }
}