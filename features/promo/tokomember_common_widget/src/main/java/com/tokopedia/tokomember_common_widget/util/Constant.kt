package com.tokopedia.tokomember_common_widget.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.IntDef
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.PREMIUM
import com.tokopedia.tokomember_common_widget.util.MemberType.Companion.VIP
import java.lang.Exception

@Retention(AnnotationRetention.SOURCE)
@IntDef(PREMIUM, VIP)
annotation class MemberType {
    companion object {
        const val PREMIUM = 0
        const val VIP = 1
    }
}

