package com.tokopedia.config.url

import android.content.Context
import android.support.annotation.GuardedBy

/**
 * @author okasurya on 4/8/19.
 */
class TokopediaUrl {
    companion object {
        private const val KEY_PREFERENCES = "ENV_PREF"
        private const val KEY_ENV = "ENV"
        private const val LIVE = "LIVE"
        private const val STAGING = "STAGING"
        private const val LOCK = "LOCK"

        @GuardedBy(LOCK)
        private var tokopediaTokopediaUrl: Url? = null

        fun init(context: Context?) {
            synchronized(LOCK) {
                if (tokopediaTokopediaUrl == null) {
                    val sharedPref = context?.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)
                    tokopediaTokopediaUrl = selectInstance(sharedPref?.getString(KEY_ENV, LIVE))
                }
            }
        }

        private fun selectInstance(env: String?): Url {
            return when(env) {
                STAGING -> staging
                else ->  live
            }
        }

        val instance: Url
            get() = synchronized(LOCK) {
                if (tokopediaTokopediaUrl == null) {
                    // if not initialized, force to Live Url
                    tokopediaTokopediaUrl = live
                    return tokopediaTokopediaUrl as Url
                }
                return tokopediaTokopediaUrl as Url
            }
    }
}
