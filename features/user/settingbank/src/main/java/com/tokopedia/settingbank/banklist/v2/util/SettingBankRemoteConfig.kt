package com.tokopedia.settingbank.banklist.v2.util

import android.content.Context

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class SettingBankRemoteConfig private constructor(val remoteConfig: FirebaseRemoteConfigImpl) {

    fun isOldFlowEnabled(): Boolean {
        return remoteConfig.getBoolean(IS_OLD_FLOW_ENABLE, false)
    }

    companion object {
        private const val IS_OLD_FLOW_ENABLE = "app_add_account_old_flow_enable"

        private var firebaseRemoteConfigImpl: SettingBankRemoteConfig? = null
        fun instance(context: Context): SettingBankRemoteConfig {
            val i = firebaseRemoteConfigImpl
            if (i != null) return i

            val j = SettingBankRemoteConfig(FirebaseRemoteConfigImpl(context))
            firebaseRemoteConfigImpl = j
            return j
        }
    }
}