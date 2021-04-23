package com.tokopedia.keys

import android.content.Context
import android.provider.Settings.Global.getString
import com.tokopedia.config.GlobalConfig

object Keys {
    private val AUTH_SCALYR_API_KEY_MA = decodeKey(SCALYR_API_KEY_MA)
    private val AUTH_SCALYR_API_KEY_PRO = decodeKey(SCALYR_API_KEY_PRO)
    private val AUTH_SCALYR_API_KEY_SA = decodeKey(SCALYR_API_KEY_SA)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_MA = decodeKey(GOOGLE_YOUTUBE_API_KEY_MA)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_PRO = decodeKey(GOOGLE_YOUTUBE_API_KEY_PRO)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_SA = decodeKey(GOOGLE_YOUTUBE_API_KEY_SA)

    @JvmField
    val NEW_RELIC = decodeKey(NEW_RELIC_APPLICATION_TOKEN)

    @JvmField
    val AUTH_TRADE_IN_API_KEY_MA = decodeKey(TRADE_IN_API_KEY_MA)

    @JvmStatic
    val AUTH_NEW_RELIC_API_KEY = decodeKey(NEW_RELIC_API_KEY)

    @JvmStatic
    val AUTH_NEW_RELIC_USER_ID = decodeKey(NEW_RELIC_USER_ID)

    @JvmStatic
    val AUTH_SCALYR_API_KEY
        get() =
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION -> {
                    AUTH_SCALYR_API_KEY_MA
                }
                GlobalConfig.SELLER_APPLICATION -> {
                    AUTH_SCALYR_API_KEY_SA
                }
                else -> {
                    AUTH_SCALYR_API_KEY_PRO
                }
            }

    @JvmStatic
    val AUTH_GOOGLE_YOUTUBE_API_KEY
        get() =
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION -> {
                    AUTH_GOOGLE_YOUTUBE_API_KEY_MA
                }
                GlobalConfig.SELLER_APPLICATION -> {
                    AUTH_GOOGLE_YOUTUBE_API_KEY_SA
                }
                else -> {
                    AUTH_GOOGLE_YOUTUBE_API_KEY_PRO
                }
            }

    @JvmStatic
    fun Context.getMoengageKey(): String {
        return when (GlobalConfig.APPLICATION_TYPE) {
            GlobalConfig.CONSUMER_APPLICATION -> {
                getString(R.string.moengage_key_ma)
            }
            GlobalConfig.SELLER_APPLICATION -> {
                getString(R.string.moengage_key_sa)
            }
            else -> {
                getString(R.string.moengage_key_pro)
            }
        }
    }

    @JvmStatic
    fun Context.getAppsFlyerKey(): String {
        return when (GlobalConfig.APPLICATION_TYPE) {
            GlobalConfig.CONSUMER_APPLICATION -> {
                getString(R.string.appsflyer_key_ma)
            }
            GlobalConfig.SELLER_APPLICATION -> {
                getString(R.string.appsflyer_key_sa)
            }
            else -> {
                getString(R.string.appsflyer_key_pro)
            }
        }
    }

    private fun decodeKey(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }
}