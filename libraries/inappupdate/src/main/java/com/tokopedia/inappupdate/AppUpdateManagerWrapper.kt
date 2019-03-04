package com.tokopedia.inappupdate

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory

/**
 * Created by hendry on 28/02/19.
 */
class AppUpdateManagerWrapper {
    companion object {
        private var appUpdateManager: AppUpdateManager? = null

        fun getInstance(context: Context): AppUpdateManager? {
            if (appUpdateManager == null) {
                appUpdateManager = AppUpdateManagerFactory.create(context.applicationContext)
            }
            return appUpdateManager
        }
    }

}