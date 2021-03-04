package com.tokopedia.vouchercreation.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.CouponType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel

class UpdateVoucherParam (
        @SerializedName("voucher_id")
        @Expose
        val voucherId: Int = 0,
        @SerializedName("benefit_idr")
        @Expose
        val benefitIdr: Int = 0,
        @SerializedName("benefit_max")
        @Expose
        val benefitMax: Int = 0,
        @SerializedName("benefit_percent")
        @Expose
        val benefitPercent: Int = 0,
        @SerializedName("benefit_type")
        @Expose
        val benefitType: String = "",
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("coupon_name")
        @Expose
        val couponName: String = "",
        @SerializedName("coupon_type")
        @Expose
        val couponType: String = "",
        @SerializedName("date_start")
        @Expose
        val dateStart: String = "",
        @SerializedName("date_end")
        @Expose
        val dateEnd: String = "",
        @SerializedName("hour_start")
        @Expose
        val hourStart: String = "",
        @SerializedName("hour_end")
        @Expose
        val hourEnd: String = "",
        @SerializedName("image")
        @Expose
        var image: String = "",
        @SerializedName("image_square")
        @Expose
        var imageSquare: String = "",
        @SerializedName("is_public")
        @Expose
        val isPublic: Int = 0,
        @SerializedName("min_purchase")
        @Expose
        val minPurchase: Int = 0,
        @SerializedName("quota")
        @Expose
        val quota: Int = 0,
        @SerializedName("token")
        @Expose
        val token: String = "",
        @SerializedName("source")
        @Expose
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
                            voucherId = voucherId,
                            benefitIdr = voucherType.value,
                            benefitMax = benefitMax,
                            benefitPercent = benefitPercent,
                            benefitType = voucherType.benefitType,
                            code = promoCode,
                            couponName = voucherName,
                            couponType = voucherType.couponType,
                            dateStart = startDate,
                            dateEnd = endDate,
                            hourStart = startHour,
                            hourEnd = endHour,
                            image = "",
                            imageSquare = "",
                            isPublic = targetType,
                            minPurchase = minPurchase,
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
                       endHour: String,
                       imageSquare: String) : UpdateVoucherParam =
                with(voucherUiModel) {
                    UpdateVoucherParam(
                            voucherId = id,
                            benefitIdr = discountAmt,
                            benefitMax = discountAmtMax,
                            benefitPercent = discountAmt,
                            benefitType = discountTypeFormatted,
                            code = code,
                            couponName = name,
                            couponType = convertVoucherToCouponDefinition(type),
                            dateStart = startDate,
                            dateEnd = endDate,
                            hourStart = startHour,
                            hourEnd = endHour,
                            image = image,
                            imageSquare = imageSquare,
                            isPublic = convertTargetType(isPublic),
                            minPurchase = minimumAmt,
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