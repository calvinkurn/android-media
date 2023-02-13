package com.tokopedia.tokomember_common_widget.util

import androidx.annotation.IntDef
import com.tokopedia.tokomember_common_widget.util.AnimationType.Companion.LEFT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.AnimationType.Companion.RIGHT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.CashbackType.Companion.IDR
import com.tokopedia.tokomember_common_widget.util.CashbackType.Companion.PERCENTAGE
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.CASHBACK
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.DISCOUNT
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.SHIPPING
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.CARD
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE_BUAT
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_MULTIPLE_EXTEND
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.COUPON_SINGLE
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PREVIEW_BUAT
import com.tokopedia.tokomember_common_widget.util.CreateScreenType.Companion.PROGRAM
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.PREMIUM
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.VIP
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CANCEL
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CREATE
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CREATE_BUAT
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CREATE_FROM_COUPON
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.DETAIL
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.EDIT
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.EXTEND
import com.tokopedia.tokomember_common_widget.util.ProgramDateType.Companion.AUTO
import com.tokopedia.tokomember_common_widget.util.ProgramDateType.Companion.MANUAL

@Retention(AnnotationRetention.SOURCE)
@IntDef(PREMIUM, VIP)
annotation class MemberType {
    companion object {
        const val PREMIUM = 0
        const val VIP = 1
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(LEFT_ANIMATION, RIGHT_ANIMATION)
annotation class AnimationType {
    companion object {
        const val LEFT_ANIMATION = 0
        const val RIGHT_ANIMATION = 1
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(CREATE, CREATE_BUAT, CREATE_FROM_COUPON, DETAIL, EXTEND, EDIT, CANCEL)
annotation class ProgramActionType {
    companion object {
        const val CREATE = 0
        const val CREATE_BUAT = 1
        const val CREATE_FROM_COUPON = 2
        const val DETAIL = 3
        const val EXTEND = 4
        const val EDIT = 5
        const val CANCEL = 6
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(CARD, PROGRAM, COUPON_SINGLE, COUPON_MULTIPLE, COUPON_MULTIPLE_BUAT, COUPON_MULTIPLE_EXTEND, PREVIEW, PREVIEW_BUAT)
annotation class CreateScreenType {
    companion object {
        const val CARD = 0
        const val PROGRAM = 1
        const val COUPON_SINGLE = 2
        const val COUPON_MULTIPLE = 3
        const val COUPON_MULTIPLE_BUAT = 4
        const val COUPON_MULTIPLE_EXTEND = 5
        const val PREVIEW = 6
        const val PREVIEW_BUAT = 7
        const val PREVIEW_EXTEND = 8
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(SHIPPING, DISCOUNT, CASHBACK)
annotation class CouponType {
    companion object {
        const val CASHBACK = 0
        const val SHIPPING = 1
        const val DISCOUNT = 2
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(IDR, PERCENTAGE)
annotation class CashbackType {
    companion object {
        const val IDR = 0
        const val PERCENTAGE = 1
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(AUTO, MANUAL)
annotation class ProgramDateType {
    companion object {
        const val AUTO = 0
        const val MANUAL = 1
    }
}


