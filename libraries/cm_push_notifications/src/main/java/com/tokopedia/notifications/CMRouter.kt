package com.tokopedia.notifications

/**
 * Created by Ashwani Tyagi on 29/10/18.
 */
interface CMRouter {
    val userId: String

    fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean

    fun getLongRemoteConfig(key: String, defaultValue: Long): Long
}
