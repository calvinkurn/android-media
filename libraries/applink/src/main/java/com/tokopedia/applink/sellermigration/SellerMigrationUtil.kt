package com.tokopedia.applink.sellermigration

import android.app.TaskStackBuilder
import android.content.Context
import android.net.Uri
import com.tokopedia.applink.RouteManager

class SellerMigrationUtil {
    fun startRedirectionActivities(context: Context, appLinks: List<String>) {
        val taskStackBuilder = TaskStackBuilder.create(context)
        appLinks.forEach {
            val intent = RouteManager.getIntent(context, Uri.parse(it).buildUpon()
                    .appendQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, "true")
                    .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_IS_AUTO_LOGIN, "true")
                    .toString())
            taskStackBuilder.addNextIntent(intent)
        }
        taskStackBuilder.startActivities()
    }
}