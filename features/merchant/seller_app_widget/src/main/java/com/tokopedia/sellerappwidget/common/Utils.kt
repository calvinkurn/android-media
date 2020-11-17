package com.tokopedia.sellerappwidget.common

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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
}