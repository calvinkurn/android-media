package com.tokopedia.tokomember_common_widget.util

import androidx.annotation.IntDef
import com.tokopedia.tokomember_common_widget.util.AnimationType.Companion.LEFT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.AnimationType.Companion.RIGHT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.CashbackType.Companion.IDR
import com.tokopedia.tokomember_common_widget.util.CashbackType.Companion.PERCENTAGE
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.CASHBACK
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.DISCOUNT
import com.tokopedia.tokomember_common_widget.util.CouponType.Companion.SHIPPING
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.PREMIUM
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.VIP
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CANCEL
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.CREATE
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.DETAIL
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.EDIT
import com.tokopedia.tokomember_common_widget.util.ProgramActionType.Companion.EXTEND
import com.tokopedia.tokomember_common_widget.util.ProgramDateType.Companion.AUTO
import com.tokopedia.tokomember_common_widget.util.ProgramDateType.Companion.MANUAL
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.CARD
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.COUPON
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.PREVIEW
import com.tokopedia.tokomember_common_widget.util.ProgramScreenType.Companion.PROGRAM

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
@IntDef(CREATE, DETAIL, EXTEND, EDIT, CANCEL)
annotation class ProgramActionType {
    companion object {
        const val CREATE = 0
        const val DETAIL = 1
        const val EXTEND = 2
        const val EDIT = 3
        const val CANCEL = 4
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(CARD, PROGRAM, COUPON, PREVIEW)
annotation class ProgramScreenType {
    companion object {
        const val CARD = 0
        const val PROGRAM = 1
        const val COUPON = 2
        const val PREVIEW = 3
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(SHIPPING, DISCOUNT, CASHBACK)
annotation class CouponType {
    companion object {
        const val SHIPPING = 0
        const val DISCOUNT = 1
        const val CASHBACK = 2
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


