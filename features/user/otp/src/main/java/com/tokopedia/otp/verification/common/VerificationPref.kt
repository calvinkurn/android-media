package com.tokopedia.otp.verification.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.boolean
import com.tokopedia.kotlin.extensions.int
import com.tokopedia.kotlin.extensions.long
import javax.inject.Inject

/**
 * Created by Ade Fulki on 02/06/20.
 */

class VerificationPref @Inject constructor(
        @ApplicationContext context: Context
) {

    private var sharedPreferences = context.getSharedPreferences(OTP_SHARED_PREF, MODE_PRIVATE)

    private var expiredTime by sharedPreferences.int(key = EXPIRED_TIME_KEY)
    private var timestamp by sharedPreferences.long(key = TIMESTAMP_KEY)
    var hasTimer by sharedPreferences.boolean(key = HAS_TIMER_KEY)

    fun setExpire(time: Int) {
        expiredTime = time
        timestamp = System.currentTimeMillis() / 1000
    }

    fun isExpired(): Boolean = ((System.currentTimeMillis() / 1000) - timestamp) > expiredTime

    fun getRemainingTime(): Int = (expiredTime - ((System.currentTimeMillis() / 1000) - timestamp)).toInt()

    companion object {
        private const val OTP_SHARED_PREF = "otp_shared_ref"
        private const val EXPIRED_TIME_KEY = "expired_time_key"
        private const val TIMESTAMP_KEY = "timestamp_key"
        private const val HAS_TIMER_KEY = "has_timer_key"
    }
}