package com.tokopedia.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.configUpdates
import kotlinx.coroutines.flow.Flow

object RemoteConfigManagerKtx {
    @JvmStatic
    fun getRemoteConfigUpdates(firebaseRemoteConfig: FirebaseRemoteConfig): Flow<ConfigUpdate> {
        return firebaseRemoteConfig.configUpdates
    }
}
