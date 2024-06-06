package com.tokopedia.sellerappwidget.common

import android.content.Context
import android.content.pm.PackageManager
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.AppWidgetTarget
import com.tokopedia.media.loader.loadImage
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 16/11/20
 */

object Utils {

    fun getAppIcon(context: Context): Int? {
        val pm = context.packageManager
        return try {
            val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            appInfo.icon
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
            null
        }
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, imgUrl: String, widgetId: Int) {
        AppWidgetTarget(context, imgViewId, remoteViews, widgetId).loadImage(context, imgUrl) {}
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, resId: Int, widgetId: Int, radius: Int) {
        AppWidgetTarget(context, imgViewId, remoteViews, widgetId).loadImage(context, resId) {
            transform(RoundedCorners(radius))
        }
    }
}
