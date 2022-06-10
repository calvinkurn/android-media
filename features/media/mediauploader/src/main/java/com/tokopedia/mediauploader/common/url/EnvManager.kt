package com.tokopedia.mediauploader.common.url

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.url.Env
import javax.inject.Inject

class EnvManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    fun getEnv(): Env {
        val sharedPref = context.getSharedPreferences(
            KEY_ENV_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val value = sharedPref
            .getString(KEY_ENV, Env.LIVE.value)

        return when(value) {
            Env.STAGING.value -> Env.STAGING
            else -> Env.LIVE
        }
    }

    companion object {
        /**
         * The key of preferences and key of variable comes from [com.tokopedia.url]
         */
        private const val KEY_ENV_PREFERENCES = "ENV_PREF"
        private const val KEY_ENV = "ENV"
    }

}