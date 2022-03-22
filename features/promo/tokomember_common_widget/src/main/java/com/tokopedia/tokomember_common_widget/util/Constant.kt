package com.tokopedia.tokomember_common_widget.util

import androidx.annotation.IntDef
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