package com.tokopedia.weaver

import com.tokopedia.remoteconfig.RemoteConfig

class WeaverFirebaseConditionCheck(keyData: String, remoteConfig: RemoteConfig?) : WeaverConditionCheckProvider<String, RemoteConfig?>(keyData, remoteConfig) {

    override fun checkCondition(): Boolean {
        return accessHelper?.getBoolean(keyData, false) ?: false
    }
}