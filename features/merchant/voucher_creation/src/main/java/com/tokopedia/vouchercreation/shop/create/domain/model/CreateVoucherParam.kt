package com.tokopedia.vouchercreation.shop.create.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherreview.VoucherReviewUiModel

data class CreateVoucherParam (
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
                            token: String) : CreateVoucherParam =
                     voucherReviewUiModel.run {
                             var benefitPercent = 0
                             var benefitMax = 0
                             if (voucherType is VoucherImageType.Percentage) {
                                     benefitPercent = (voucherType as? VoucherImageType.Percentage)?.percentage ?: 0
                                     benefitMax = (voucherType as? VoucherImageType.Percentage)?.value ?: 0
                             }
                             CreateVoucherParam(
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

        }
}