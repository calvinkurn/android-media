package com.tokopedia.analytics.cashshield

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_CASHSHIELD

/**
 * @author okasurya on 2019-07-11.
 */
class CashShield(val context: Context) {
    private val cashShieldScope by lazy {
        CashShieldScope(context)
    }

    private val remoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    fun send() {
        if (remoteConfig.getBoolean(ENABLE_CASHSHIELD, false)) {
            cashShieldScope.send()
        }
    }

    fun cancel() {
        cashShieldScope.cancel()
    }

    fun getSessionId(): String {
        return cashShieldScope.getSessionId()
    }

    fun refreshSession() {
        return cashShieldScope.refreshSession()
    }
}