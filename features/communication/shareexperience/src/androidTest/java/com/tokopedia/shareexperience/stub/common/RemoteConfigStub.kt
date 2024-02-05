package com.tokopedia.shareexperience.stub.common

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class RemoteConfigStub @Inject constructor() : RemoteConfig {

    val mutableMap: MutableMap<String, Any> = mutableMapOf()

    override fun getKeysByPrefix(prefix: String): MutableSet<String> {
        return mutableSetOf()
    }

    override fun getBoolean(key: String): Boolean {
        return (mutableMap[key] as? Boolean) ?: false
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return (mutableMap[key] as? Boolean) ?: defaultValue
    }

    override fun getDouble(key: String): Double {
        return (mutableMap[key] as? Double) ?: 0.0
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return (mutableMap[key] as? Double) ?: defaultValue
    }

    override fun getLong(key: String): Long {
        return (mutableMap[key] as? Long) ?: 0
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return (mutableMap[key] as? Long) ?: defaultValue
    }

    override fun getString(key: String): String {
        return (mutableMap[key] as? String) ?: ""
    }

    override fun getString(key: String, defaultValue: String): String {
        return (mutableMap[key] as? String) ?: defaultValue
    }

    override fun setString(key: String, value: String) {
        mutableMap[key] = value
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        // no-op
    }
}
