package com.tokopedia.config.url

import android.content.Context

/**
 * @author okasurya on 4/8/19.
 */
class TokopediaUrl {

    companion object {

        private val lock = Any()

        @Volatile private var tokopediaUrl: Url? = null

        fun init(context: Context) : Url {
            return tokopediaUrl?: synchronized(lock) {
                getEnvironment(context).also {
                    tokopediaUrl = it
                }
            }
        }

        fun getInstance() : Url {
            return tokopediaUrl?: live
        }

        fun deleteInstance() {
            synchronized(lock) {
                tokopediaUrl = null
            }
        }

        private fun getEnvironment(context: Context) : Url {
            val sharedPreferences = context.getSharedPreferences(KEY_ENV_PREFERENCES,
                    Context.MODE_PRIVATE)
            return selectInstance(sharedPreferences.getString(KEY_ENV, Env.LIVE.value))
        }

        fun selectInstance(env: String?): Url {
            return when(env) {
                Env.STAGING.value -> staging
                else ->  live
            }
        }

        fun setEnvironment(context: Context, env: Env) {
            val sharedPreferences = context.getSharedPreferences(KEY_ENV_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(KEY_ENV, env.value).apply()
        }
    }
}
