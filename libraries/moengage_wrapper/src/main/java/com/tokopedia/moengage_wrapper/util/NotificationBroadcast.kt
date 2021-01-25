package com.tokopedia.moengage_wrapper.util

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.moengage_wrapper.*
import com.tokopedia.moengage_wrapper.MoengageInteractor.sendTrackEvent
import java.util.*

class NotificationBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val action = intent.action ?: return
            val notificationId = intent.extras!!.getInt(EXTRA_NOTIFICATION_ID)
            when (action) {
                ACTION_DELETE_NOTIFY -> cancelNotification(context, notificationId)
                ACTION_GRID_CLICK -> handleGridClick(context, intent, notificationId)
                ACTION_PERSISTENT_CLICK -> handlePersistentClick(context, intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePersistentClick(context: Context, intent: Intent) {
        try {
            val iconName = intent.getStringExtra(EXTRA_PERSISTENT_ICON_NAME)
            val iconUrl = intent.getStringExtra(EXTRA_PERSISTENT_ICON_URL)
            val deepLink = intent.getStringExtra(EXTRA_PERSISTENT_DEEPLINK)
            val campaignId = intent.getStringExtra(EXTRA_CAMPAIGN_ID)
            val appLinkIntent = RouteManager.getIntent(context.applicationContext, deepLink)
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appLinkIntent.putExtras(intent.extras!!)
            context.startActivity(appLinkIntent)
            context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            val attributeMap: MutableMap<String, Any> = HashMap()
            attributeMap[EXTRA_PERSISTENT_DEEPLINK] = deepLink ?: ""
            attributeMap[EXTRA_PERSISTENT_ICON_NAME] = iconName ?: ""
            attributeMap[EXTRA_PERSISTENT_ICON_URL] = iconUrl ?: ""
            attributeMap[EXTRA_CAMPAIGN_ID] = campaignId ?: ""
            postMoengageEvent(attributeMap)
        } catch (e: Exception) {
        }
    }

    private fun handleGridClick(context: Context, intent: Intent, notificationId: Int) {
        val deepLink = intent.extras!!.getString(EXTRA_DEEP_LINK)
        try {
            val appLinkIntent = RouteManager.getIntent(context.applicationContext, deepLink)
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appLinkIntent.putExtras(intent.extras!!)
            context.startActivity(appLinkIntent)
        } catch (e: ActivityNotFoundException) {
        }
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        cancelNotification(context, notificationId)
    }

    private fun cancelNotification(context: Context, notificationId: Int) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(notificationId)
    }

    private fun postMoengageEvent(attributeMap: Map<String, Any>) {
        sendTrackEvent(EVENT_PERSISTENT_CLICK_NAME, attributeMap)
    }
}