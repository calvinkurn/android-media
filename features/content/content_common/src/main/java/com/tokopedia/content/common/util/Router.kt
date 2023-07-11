package com.tokopedia.content.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.tokopedia.applink.RouteManager
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 15/11/22
 */
@Reusable
class Router @Inject constructor() {

    fun route(context: Context?, appLinkPattern: String, vararg parameter: String) {
        RouteManager.route(context, appLinkPattern, *parameter)
    }

    fun route(activity: Activity, intent: Intent, requestCode: Int? = null) {
        if(requestCode == null) {
            activity.startActivity(intent)
        }
        else {
            activity.startActivityForResult(intent, requestCode)
        }
    }

    fun route(activityResultLauncher: ActivityResultLauncher<Intent>, intent: Intent) {
        activityResultLauncher.launch(intent)
    }

    fun route(
        context: Context,
        activityResultLauncher: ActivityResultLauncher<Intent>,
        appLinkPattern: String,
        vararg parameter: String
    ) {
        activityResultLauncher.launch(getIntent(context, appLinkPattern, *parameter))
    }

    fun getIntent(context: Context?, deepLinkPattern: String, vararg parameter: String): Intent {
        return RouteManager.getIntent(context, deepLinkPattern, *parameter)
    }
}
