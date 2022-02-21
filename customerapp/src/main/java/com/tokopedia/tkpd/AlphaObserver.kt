package com.tokopedia.tkpd

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

const val CHANNEL_ID = "alpha"
const val NOTIFICATION_ID = 123 shr 5

class AlphaObserver : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        setWindowAlpha(activity)
        showBanner(activity)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun showBanner(context: Context) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.alpha_notification_category),
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        val remoteView = RemoteViews(context.packageName, R.layout.alpha_notification_layout)
        remoteView.setTextViewText(R.id.mynotifyexpnd, context.getString(R.string.tokopedia_alpha))
        val mBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setCustomContentView(remoteView)
                .setSmallIcon(R.drawable.ic_alpha)
                .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
    }

    private fun setWindowAlpha(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = activity.window.decorView as ViewGroup
            val layoutWrapper = LinearLayout(activity)

            layoutWrapper.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
            layoutWrapper.gravity = Gravity.CENTER_HORIZONTAL
            val tv = TextView(activity)
            tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getStatusBarHeight(activity))
            layoutWrapper.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark))
            tv.text = activity.resources.getString(R.string.tokopedia_alpha)
            tv.setTextColor(Color.BLACK)
            tv.gravity = Gravity.CENTER
            layoutWrapper.addView(tv)
            decorView.addView(layoutWrapper)
            decorView.invalidate()
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun getStatusBarHeight(activity: Activity): Int {
        var titleBarHeigh = 0
        val dipsMap: MutableMap<Float, Int> = mutableMapOf()
        if (titleBarHeigh > 0) return titleBarHeigh
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        titleBarHeigh = if (resourceId > 0) {
            activity.resources.getDimensionPixelSize(resourceId)
        } else convertDpToPixel(25f, activity)
        return titleBarHeigh
    }

    private fun convertDpToPixel(dp: Float, activity: Activity): Int {
        val dipsMap: MutableMap<Float, Int> = mutableMapOf()
        if (dipsMap.containsKey(dp)) return dipsMap[dp]!!
        val resources = activity.resources
        val metrics = resources.displayMetrics
        val value = (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        dipsMap[dp] = value
        return value
    }
}