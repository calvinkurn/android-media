package com.tokopedia.sessioncommon.data

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 04/02/19.
 */
open class Token {

    companion object {
        private const val DISABLE_GOOGLE_NEW_CLIENT_ID = "android_main_app_disabled_new_google_client_id"
        private const val OLD_GOOGLE_API_KEY = "692092518182-rjgh0bja6q41dllpq2dptn134cmhiv9h.apps.googleusercontent.com"
        private const val GOOGLE_API_KEY_STAGING = "692092518182-ftki1chbj3oemudv5ud7mdnieqe16u7e.apps.googleusercontent.com"
        private const val GOOGLE_API_KEY = "692092518182-h6a8jufa9bl8mfuvcae95qa92cbmes02.apps.googleusercontent.com"

        fun getGoogleClientId(context: Context?): String {
            return try {
                val remoteConfig = FirebaseRemoteConfigImpl(context)
                if(remoteConfig.getBoolean(DISABLE_GOOGLE_NEW_CLIENT_ID, false)) {
                    OLD_GOOGLE_API_KEY
                } else {
                    if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
                        GOOGLE_API_KEY_STAGING
                    } else {
                        GOOGLE_API_KEY
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                OLD_GOOGLE_API_KEY
            }
        }
    }

}