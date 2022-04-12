package com.tokopedia.tokomember_common_widget.util

import androidx.annotation.IntDef
import com.tokopedia.tokomember_common_widget.util.ANIMATIONTYPE.Companion.LEFT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.ANIMATIONTYPE.Companion.RIGHT_ANIMATION
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.PREMIUM
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.VIP

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
annotation class ANIMATIONTYPE {
    companion object {
        const val LEFT_ANIMATION = 0
        const val RIGHT_ANIMATION = 1
    }
}

