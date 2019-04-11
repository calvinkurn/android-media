package com.tokopedia.config.url

import android.content.Context
import android.support.annotation.GuardedBy

/**
 * @author okasurya on 4/8/19.
 */
class TokopediaUrl {
    companion object {
        private const val LOCK = "LOCK"

        @GuardedBy(LOCK)
        @Volatile
        private var tokopediaUrl: Url? = null

        fun init(context: Context?) {
            synchronized(LOCK) {
                if (tokopediaUrl == null) {
                    val sharedPref = context?.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
                    tokopediaUrl = selectInstance(sharedPref?.getString(KEY_ENV, Env.LIVE.value))
                }
            }
        }

        internal fun selectInstance(env: String?): Url {
            return when(env) {
                Env.STAGING.value -> staging
                else ->  live
            }
        }

        val url: Url
            get() = synchronized(LOCK) {
                if (tokopediaUrl == null) {
                    // if not initialized, force to Live Url
                    tokopediaUrl = live
                    return tokopediaUrl as Url
                }
                return tokopediaUrl as Url
            }

        fun setEnvironment(context: Context?, env: Env) {
            val sharedPref = context?.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString(KEY_ENV, env.value)?.commit()
        }
    }
}
