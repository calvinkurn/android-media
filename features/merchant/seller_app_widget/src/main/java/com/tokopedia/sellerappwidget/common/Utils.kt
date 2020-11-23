package com.tokopedia.sellerappwidget.common

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 16/11/20
 */

object Utils {

    fun getAppIcon(context: Context): Drawable? {
        val pm = context.packageManager
        try {
            val appInfo: ApplicationInfo? = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)

            appInfo?.let {
                return pm.getApplicationIcon(it)
            }

            return null
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }

    fun formatDate(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, imgUrl: String, widgetId: Int) {
        val awt = AppWidgetTarget(context.applicationContext, imgViewId, remoteViews, widgetId)
        Glide.with(context.applicationContext)
                .asBitmap()
                .load(imgUrl)
                .into(awt)
    }

    fun loadImageIntoAppWidget(context: Context, remoteViews: RemoteViews, imgViewId: Int, drawable: Drawable, widgetId: Int) {
        val awt = AppWidgetTarget(context.applicationContext, imgViewId, remoteViews, widgetId)
        Glide.with(context.applicationContext)
                .asBitmap()
                .load(drawable)
                .into(awt)
    }
}