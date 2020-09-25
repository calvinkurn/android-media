package com.tokopedia.promotionstarget.data.coupon

import androidx.annotation.IntDef
import com.google.gson.annotations.SerializedName

data class TokopointsCouponDetailResponse(@SerializedName("hachikoCouponDetail") val coupon:TokopointsCouponDetail?)

@Retention(AnnotationRetention.SOURCE)
@IntDef(CouponStatusType.USED, CouponStatusType.EXPIRED, CouponStatusType.INACTIVE, CouponStatusType.ACTIVE, CouponStatusType.UNKNOWN)
annotation class CouponStatusType {
    companion object {
        const val USED = 5
        const val EXPIRED = 6
        const val INACTIVE = 7
        const val ACTIVE = 8
        const val UNKNOWN = -99
    }
}