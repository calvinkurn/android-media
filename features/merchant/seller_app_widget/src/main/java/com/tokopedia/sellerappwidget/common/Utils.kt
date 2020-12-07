package com.tokopedia.sellerappwidget.common

import android.content.Context
import android.content.pm.PackageManager
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.AppWidgetTarget
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

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

    fun formatDate(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, imgUrl: String, widgetId: Int) {
        val awt = AppWidgetTarget(context, imgViewId, remoteViews, widgetId)
        Glide.with(context)
                .asBitmap()
                .load(imgUrl)
                .into(awt)
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, resId: Int, widgetId: Int, radius: Int) {
        val awt = AppWidgetTarget(context, imgViewId, remoteViews, widgetId)
        Glide.with(context)
                .asBitmap()
                .load(resId)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(awt)
    }
}