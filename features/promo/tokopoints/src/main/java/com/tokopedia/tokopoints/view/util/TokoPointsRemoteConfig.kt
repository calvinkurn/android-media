package com.tokopedia.tokopoints.view.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class  TokoPointsRemoteConfig  private constructor(val remoteConfig: FirebaseRemoteConfigImpl){

     fun getLongRemoteConfig(key: String, defaultValue: Long): Long {
        return remoteConfig.getLong(key, defaultValue)
    }

     fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean {
        return remoteConfig.getBoolean(key, defaultValue)
    }
    companion object{
        private var firebaseRemoteConfigImpl : TokoPointsRemoteConfig? = null
        fun instance(context: Context) : TokoPointsRemoteConfig{
            val i = firebaseRemoteConfigImpl
            if (i != null) return i

            val j = TokoPointsRemoteConfig(FirebaseRemoteConfigImpl(context))
            firebaseRemoteConfigImpl = j
            return j
        }
    }
}
