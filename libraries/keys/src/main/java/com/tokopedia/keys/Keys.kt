package com.tokopedia.keys

import android.content.Context
import com.tokopedia.config.GlobalConfig

object Keys {
    private val AUTH_SCALYR_API_KEY_MA = decodeKey(SCALYR_API_KEY_MA)
    private val AUTH_SCALYR_API_KEY_PRO = decodeKey(SCALYR_API_KEY_PRO)
    private val AUTH_SCALYR_API_KEY_SA = decodeKey(SCALYR_API_KEY_SA)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_MA = decodeKey(GOOGLE_YOUTUBE_API_KEY_MA)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_PRO = decodeKey(GOOGLE_YOUTUBE_API_KEY_PRO)
    private val AUTH_GOOGLE_YOUTUBE_API_KEY_SA = decodeKey(GOOGLE_YOUTUBE_API_KEY_SA)

    private val AUTH_NEW_RELIC_API_KEY_MA = decodeKey(NEW_RELIC_API_KEY_MA)
    private val AUTH_NEW_RELIC_API_KEY_PRO = decodeKey(NEW_RELIC_API_KEY_PRO)
    private val AUTH_NEW_RELIC_API_KEY_SA = decodeKey(NEW_RELIC_API_KEY_SA)

    private val AUTH_NEW_RELIC_USER_ID_MA = decodeKey(NEW_RELIC_USER_ID_MA)
    private val AUTH_NEW_RELIC_USER_ID_PRO = decodeKey(NEW_RELIC_USER_ID_PRO)
    private val AUTH_NEW_RELIC_USER_ID_SA = decodeKey(NEW_RELIC_USER_ID_SA)

    private val AUTH_CHROMECAST_APPLICATION_ID_MA = decodeKey(CHROMECAST_APPLICATION_ID_MA)
    private val AUTH_CHROMECAST_APPLICATION_ID_PRO = decodeKey(CHROMECAST_APPLICATION_ID_PRO)
    private val AUTH_CHROMECAST_APPLICATION_ID_SA = decodeKey(CHROMECAST_APPLICATION_ID_SA)

    @JvmField
    val LSDK_KEY_MA = decodeKey(LSDK_KEY)

    @JvmField
    val GTP_KEY_MA = decodeKey(GTP_KEY)

    @JvmField
    val NEW_RELIC_TOKEN_SA = decodeKey(NEW_RELIC_APPLICATION_TOKEN_SA)

    @JvmField
    val NEW_RELIC_TOKEN_MA = decodeKey(NEW_RELIC_APPLICATION_TOKEN_MA)

    @JvmStatic
    val AUTH_NEW_RELIC_API_KEY
        get() =
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION -> {
                    AUTH_NEW_RELIC_API_KEY_MA
                }
                GlobalConfig.SELLER_APPLICATION -> {
                    AUTH_NEW_RELIC_API_KEY_SA
                }
                else -> {
                    AUTH_NEW_RELIC_API_KEY_PRO
                }
            }

    @JvmStatic
    val AUTH_NEW_RELIC_USER_ID
        get() =
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION -> {
                    AUTH_NEW_RELIC_USER_ID_MA
                }
                GlobalConfig.SELLER_APPLICATION -> {
                    AUTH_NEW_RELIC_USER_ID_SA
                }
                else -> {
                    AUTH_NEW_RELIC_USER_ID_PRO
                }
            }

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

    @JvmStatic
    val CHROMECAST_APPLICATION_ID
        get() =
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION -> {
                    AUTH_CHROMECAST_APPLICATION_ID_MA
                }
                GlobalConfig.SELLER_APPLICATION -> {
                    AUTH_CHROMECAST_APPLICATION_ID_SA
                }
                else -> {
                    AUTH_CHROMECAST_APPLICATION_ID_PRO
                }
            }

    private fun decodeKey(keys: IntArray): String {
        return keys.joinToString(separator = "") { it.toChar().toString() }
    }
}
