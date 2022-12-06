package com.tokopedia.mvc.util.constant

import androidx.annotation.StringDef

object VoucherDefinition {

    fun convertVoucherToCouponDefinition(@VoucherTypeConst type: Int): String {
        return when (type) {
            VoucherTypeConst.FREE_ONGKIR -> CouponType.SHIPPING
            VoucherTypeConst.DISCOUNT -> CouponType.DISCOUNT
            VoucherTypeConst.CASHBACK -> CouponType.CASHBACK
            else -> CouponType.SHIPPING
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(CouponType.SHIPPING, CouponType.CASHBACK, CouponType.DISCOUNT)
    annotation class CouponType {
        companion object {
            const val SHIPPING = "shipping"
            const val DISCOUNT = "discount"
            const val CASHBACK = "cashback"
        }
    }
}
