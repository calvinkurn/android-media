package com.tokopedia.topchat.stub.common

import com.tokopedia.remoteconfig.RemoteConfig

class RemoteConfigStub: RemoteConfig {

    private var stringResult: String = ""
    private var longResult: Long = 0
    private var doubleResult: Double = 0.0
    private var booleanResult: Boolean = true

    fun setStringResult(result: String) {
        this.stringResult = result
    }

    fun setLongResult(result: Long) {
        this.longResult = result
    }

    fun setDoubleResult(result: Double) {
        this.doubleResult = result
    }

    fun setBooleanResult(result: Boolean) {
        this.booleanResult = result
    }

    override fun getKeysByPrefix(prefix: String?): MutableSet<String> {
        return mutableSetOf()
    }

    override fun getBoolean(key: String?): Boolean {
        return booleanResult
    }

    override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return booleanResult
    }

    override fun getDouble(key: String?): Double {
        return doubleResult
    }

    override fun getDouble(key: String?, defaultValue: Double): Double {
        return doubleResult
    }

    override fun getLong(key: String?): Long {
        return longResult
    }

    override fun getLong(key: String?, defaultValue: Long): Long {
        return longResult
    }

    override fun getString(key: String?): String {
        return stringResult
    }

    override fun getString(key: String?, defaultValue: String?): String {
        return stringResult
    }

    override fun setString(key: String?, value: String?) {}

    override fun fetch(listener: RemoteConfig.Listener?) {}
}