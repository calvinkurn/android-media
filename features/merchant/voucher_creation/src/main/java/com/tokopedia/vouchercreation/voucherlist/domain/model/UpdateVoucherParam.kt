package com.tokopedia.vouchercreation.voucherlist.domain.model

import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.domain.model.VoucherSource
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.enums.CouponType
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
        val image: String = "",
        val image_square: String = "",
        val is_public: Int = 0,
        val min_purchase: Int = 0,
        val quota: Int = 0,
        val token: String = "",
        val source: String = "") {

    companion object {

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