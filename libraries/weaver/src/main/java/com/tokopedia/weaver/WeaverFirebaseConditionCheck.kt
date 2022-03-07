package com.tokopedia.weaver

import com.tokopedia.remoteconfig.RemoteConfig

class WeaverFirebaseConditionCheck(
    keyData: String,
    remoteConfig: RemoteConfig?,
    defaultValue: Boolean = false) : WeaverConditionCheckProvider<String, RemoteConfig?, Boolean>(keyData, remoteConfig, defaultValue) {
    override fun checkCondition(): Boolean {
        return accessHelper?.getBoolean(keyData, defaultValue) ?: defaultValue
    }
}