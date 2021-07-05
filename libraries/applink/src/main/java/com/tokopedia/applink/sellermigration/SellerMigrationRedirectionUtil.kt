package com.tokopedia.applink.sellermigration

import android.app.TaskStackBuilder
import android.content.Context
import com.tokopedia.applink.RouteManager

class SellerMigrationRedirectionUtil {
    fun startRedirectionActivities(context: Context, appLinks: List<String>) {
        val taskStackBuilder = TaskStackBuilder.create(context)
        appLinks.forEach {
            taskStackBuilder.addNextIntent(RouteManager.getIntent(context, it))
        }
        taskStackBuilder.startActivities()
    }
}