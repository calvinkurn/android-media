package com.tokopedia.productcard.test

import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.remoteconfig.RemoteConfig

internal class ProductCardDummyRemoteConfig(
    private val getBoolean: (key: String?, defaultValue: Boolean) -> Boolean = { _, _ -> false },
    private val getString: (key: String?, defaultValue: String?) -> String = { _, _ -> "" },
) {

    fun get(): Lazy<RemoteConfig?> = lazyThreadSafetyNone {
        object: RemoteConfig {
            override fun getKeysByPrefix(prefix: String?): MutableSet<String> = mutableSetOf()

            override fun getBoolean(key: String?): Boolean = getBoolean(key, false)

            override fun getBoolean(key: String?, defaultValue: Boolean): Boolean =
                getBoolean.invoke(key, defaultValue)

            override fun getDouble(key: String?): Double = 0.0

            override fun getDouble(key: String?, defaultValue: Double): Double = 0.0

            override fun getLong(key: String?): Long = 0L

            override fun getLong(key: String?, defaultValue: Long): Long = 0L

            override fun getString(key: String?): String = getString(key, "")

            override fun getString(key: String?, defaultValue: String?): String =
                getString.invoke(key, defaultValue)

            override fun setString(key: String?, value: String?) {

            }

            override fun fetch(listener: RemoteConfig.Listener?) {

            }
        }
    }
}
