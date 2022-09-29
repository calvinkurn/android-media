package com.tokopedia.developer_options.config

import android.content.Context

/**
 * Created By : Jonathan Darwin on September 29, 2022
 */
object DevOptConfig {

    private const val CHUCK_ENABLED = "CHUCK_ENABLED"
    private const val IS_CHUCK_ENABLED = "is_enable"

    private const val DEV_OPT_ON_NOTIF_ENABLED = "DEV_OPT_ON_NOTIF_ENABLED"
    private const val IS_DEV_OPT_ON_NOTIF_ENABLED = "IS_DEV_OPT_ON_NOTIF_ENABLED"

    fun isChuckNotifEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_CHUCK_ENABLED, false)
    }

    fun setChuckNotifEnabled(context: Context, isChuckNotifEnabled: Boolean) {
        context.getSharedPreferences(CHUCK_ENABLED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(
                IS_CHUCK_ENABLED,
                isChuckNotifEnabled
            )
            .apply()
    }

    fun isDevOptOnNotifEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_NOTIF_ENABLED, true)
    }

    fun setDevOptOnNotifEnabled(context: Context, isDevOptOnNotifEnabled: Boolean) {
        context.getSharedPreferences(DEV_OPT_ON_NOTIF_ENABLED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(
                IS_DEV_OPT_ON_NOTIF_ENABLED,
                isDevOptOnNotifEnabled
            )
            .apply()
    }
}
