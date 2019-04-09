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
        private var url: Url? = null

        fun init(context: Context?) {
            synchronized(LOCK) {
                if (url == null) {
                    val sharedPref = context?.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
                    url = selectInstance(sharedPref?.getString(KEY_ENV, Env.LIVE.value))
                }
            }
        }

        private fun selectInstance(env: String?): Url {
            return when(env) {
                Env.STAGING.value -> staging
                else ->  live
            }
        }

        val instance: Url
            get() = synchronized(LOCK) {
                if (url == null) {
                    // if not initialized, force to Live Url
                    url = live
                    return url as Url
                }
                return url as Url
            }

        fun setEnvironment(context: Context?, env: Env) {
            val sharedPref = context?.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString(KEY_ENV, env.value)?.commit()
        }
    }
}
