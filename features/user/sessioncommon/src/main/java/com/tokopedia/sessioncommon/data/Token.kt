package com.tokopedia.sessioncommon.data

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

/**
 * @author by nisie on 04/02/19.
 */
open class Token {

    companion object {
        private const val GOOGLE_API_KEY_STAGING = "692092518182-ftki1chbj3oemudv5ud7mdnieqe16u7e.apps.googleusercontent.com"
        private const val GOOGLE_API_KEY = "692092518182-h6a8jufa9bl8mfuvcae95qa92cbmes02.apps.googleusercontent.com"

        fun getGoogleClientId(): String = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            GOOGLE_API_KEY_STAGING
        } else {
            GOOGLE_API_KEY
        }
    }

}