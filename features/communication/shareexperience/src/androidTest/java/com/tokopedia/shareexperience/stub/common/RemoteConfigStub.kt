package com.tokopedia.shareexperience.stub.common

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

class RemoteConfigStub @Inject constructor() : RemoteConfig {
    override fun getKeysByPrefix(prefix: String?): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDouble(key: String?): Double {
        TODO("Not yet implemented")
    }

    override fun getDouble(key: String?, defaultValue: Double): Double {
        TODO("Not yet implemented")
    }

    override fun getLong(key: String?): Long {
        TODO("Not yet implemented")
    }

    override fun getLong(key: String?, defaultValue: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getString(key: String?): String {
        TODO("Not yet implemented")
    }

    override fun getString(key: String?, defaultValue: String?): String {
        TODO("Not yet implemented")
    }

    override fun setString(key: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun fetch(listener: RemoteConfig.Listener?) {
        TODO("Not yet implemented")
    }
}
