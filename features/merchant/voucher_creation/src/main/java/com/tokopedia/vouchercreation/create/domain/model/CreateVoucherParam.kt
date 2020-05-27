package com.tokopedia.vouchercreation.create.domain.model

import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel

data class CreateVoucherParam (
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
        val image: String = "",
        val image_square: String = "",
        val is_public: Int = 0,
        val min_purchase: Int = 0,
        val quota: Int = 0,
        val token: String = "",
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

        }
}