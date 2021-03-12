package com.tokopedia.moengage_wrapper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.moe.pushlibrary.utils.MoEHelperUtils
import com.moengage.core.ConfigurationProvider
import com.moengage.pushbase.push.PushMessageListener
import com.tokopedia.moengage_wrapper.interfaces.CustomPushDataListener


const val EXTRA_NOTIFICATION_ID = "extra_delete_notification_id"
const val ACTION_DELETE_NOTIFY = "com.tokopedia.moengage_wrapper.util.delete"
const val ACTION_GRID_CLICK = "action_grid_click"
const val EXTRA_DEEP_LINK = "extra_deep_link"
const val EVENT_PERSISTENT_CLICK_NAME = "Click_Action_Persistent"
const val ACTION_PERSISTENT_CLICK = "extra_action_persistent_click"
const val EXTRA_PERSISTENT_ICON_NAME = "icon_name"
const val EXTRA_PERSISTENT_ICON_URL = "icon_url"
const val EXTRA_PERSISTENT_DEEPLINK = "deeplink"
const val EXTRA_CAMPAIGN_ID = "campaign_id"
private const val NOTIFICATION_ID = 777
private const val GRID_NOTIFICATION_ID = 778
private const val KEY_ICON_NAME1 = "icon_name1"
private const val KEY_ICON_NAME2 = "icon_name2"
private const val KEY_ICON_NAME3 = "icon_name3"
private const val KEY_ICON_NAME4 = "icon_name4"
private const val KEY_DEEPLINK1 = "deeplink1"
private const val KEY_DEEPLINK2 = "deeplink2"
private const val KEY_DEEPLINK3 = "deeplink3"
private const val KEY_DEEPLINK4 = "deeplink4"
private const val KEY_DEEPLINK5 = "deeplink5"
private const val KEY_DEEPLINK6 = "deeplink6"
private const val KEY_ICON_URL1 = "icon_url1"
private const val KEY_ICON_URL2 = "icon_url2"
private const val KEY_ICON_URL3 = "icon_url3"
private const val KEY_ICON_URL4 = "icon_url4"
private const val KEY_ICON_URL5 = "icon_url5"
private const val KEY_ICON_URL6 = "icon_url6"
const val KEY_CAMPAIGN_ID = "gcm_campaign_id"
const val KEY_IS_PERSISTENT = "is_persistent"
const val KEY_IS_GRID = "is_grid"
const val PERSISTENT = "1"
const val GRID = "1"

class CustomPushListener(val listener: CustomPushDataListener) : PushMessageListener() {

    // if the app wants to add additional properties to the NotificationCompat.Builder
    override fun onCreateNotification(context: Context, extras: Bundle,
                                      provider: ConfigurationProvider): NotificationCompat.Builder? {

        val builder = super.onCreateNotification(context, extras, provider)
        if (PERSISTENT.equals(extras.getString(KEY_IS_PERSISTENT), ignoreCase = true)) {
            provider.updateNotificationId(NOTIFICATION_ID)
            createPersistentNotification(context, extras, builder)
        } else if (GRID.equals(extras.getString(KEY_IS_GRID), ignoreCase = true)) {
            provider.updateNotificationId(GRID_NOTIFICATION_ID)
            createGridNotification(context, extras, builder)
        }
        return builder
    }


    private fun createPersistentNotification(context: Context, extras: Bundle, builder: NotificationCompat.Builder) {
        val `when` = System.currentTimeMillis()
        var campaignId: String? = ""
        if (extras.containsKey(KEY_CAMPAIGN_ID)) {
            campaignId = extras.getString(KEY_CAMPAIGN_ID)
        }
        val remoteView = RemoteViews(context.packageName, R.layout.persistent_notification_layout)
        val titles = arrayOf(
                extras.getString(KEY_ICON_NAME1), extras.getString(KEY_ICON_NAME2),
                extras.getString(KEY_ICON_NAME3), extras.getString(KEY_ICON_NAME4)
        )
        val deepLinks = arrayOf(
                extras.getString(KEY_DEEPLINK1), extras.getString(KEY_DEEPLINK2),
                extras.getString(KEY_DEEPLINK3), extras.getString(KEY_DEEPLINK4)
        )
        val iconUrls = arrayOf(
                extras.getString(KEY_ICON_URL1), extras.getString(KEY_ICON_URL2),
                extras.getString(KEY_ICON_URL3), extras.getString(KEY_ICON_URL4)
        )
        val titleResIds = arrayOf(
                R.id.title1, R.id.title2, R.id.title3, R.id.title4
        )
        val iconResIds = arrayOf(
                R.id.image_icon1, R.id.image_icon2, R.id.image_icon3, R.id.image_icon4
        )
        val containerResIds = arrayOf(
                R.id.lin_container_1, R.id.lin_container_2, R.id.lin_container_3, R.id.lin_container_4
        )
        for (i in 0..3) {
            if (!TextUtils.isEmpty(titles[i]) && !TextUtils.isEmpty(deepLinks[i]) && !TextUtils.isEmpty(iconUrls[i])) {
                remoteView.setTextViewText(titleResIds[i], titles[i])
                remoteView.setImageViewBitmap(iconResIds[i], MoEHelperUtils.downloadImageBitmap(iconUrls[i]))
                val pIntent = getPersistentClickPendingIntent(
                        context, iconUrls[i], titles[i], deepLinks[i], campaignId, 100 + i)
                remoteView.setOnClickPendingIntent(containerResIds[i], pIntent)
            }
        }
        val configurationProvider = ConfigurationProvider.getInstance(context)
        configurationProvider.updateNotificationId(NOTIFICATION_ID)
        val deleteIntent = listener.getNotificationBroadcastIntent()
        deleteIntent.action = ACTION_DELETE_NOTIFY
        deleteIntent.putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID)
        val deletePendingIntent = getPendingIntent(context, 107, deleteIntent)
        remoteView.setOnClickPendingIntent(R.id.lin_container_5, deletePendingIntent)
        val notificationIntent = listener.getPersistentNotificationIntent()
        val contentIntent = PendingIntent.getActivity(context, 104,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.image_icon6, contentIntent)
        builder.setSmallIcon(listener.getIcStatNotifyWhiteDrawable())
                .setCustomContentView(remoteView)
                .setCustomBigContentView(remoteView)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setWhen(`when`)
    }

    /**
     * it will create grid notification (2,3,6) images which will have [PendingIntent]
     * setOnClickPendingIntent
     *
     * @param context This context is used to create intent, to get packageName
     * @param extras  This bundle contains the payload for Grid Type Notification
     * @param builder This builder object is provided by MoEngage and here we are setting contentView
     */
    private fun createGridNotification(context: Context, extras: Bundle, builder: NotificationCompat.Builder) {
        val `when` = System.currentTimeMillis()
        val expandRemoteView = RemoteViews(context.packageName,
                R.layout.grid_notificaiton_layout)
        setCollapseData(context, expandRemoteView, extras)
        val deeplink1 = extras.getString(KEY_DEEPLINK1)
        val deeplink2 = extras.getString(KEY_DEEPLINK2)
        val deeplink3 = extras.getString(KEY_DEEPLINK3)
        val deeplink4 = extras.getString(KEY_DEEPLINK4)
        val deeplink5 = extras.getString(KEY_DEEPLINK5)
        val deeplink6 = extras.getString(KEY_DEEPLINK6)
        val url1 = extras.getString(KEY_ICON_URL1)
        val url2 = extras.getString(KEY_ICON_URL2)
        val url3 = extras.getString(KEY_ICON_URL3)
        val url4 = extras.getString(KEY_ICON_URL4)
        val url5 = extras.getString(KEY_ICON_URL5)
        val url6 = extras.getString(KEY_ICON_URL6)
        createGrid(context, expandRemoteView, R.id.iv_gridOne, url1, deeplink1, 201)
        createGrid(context, expandRemoteView, R.id.iv_gridTwo, url2, deeplink2, 202)
        createGrid(context, expandRemoteView, R.id.iv_gridThree, url3, deeplink3, 203)
        if (null != url4 && null != url5 && null != url6) {
            createGrid(context, expandRemoteView, R.id.iv_gridFour, url4, deeplink4, 204)
            createGrid(context, expandRemoteView, R.id.iv_gridFive, url5, deeplink5, 205)
            createGrid(context, expandRemoteView, R.id.iv_gridSix, url6, deeplink6, 206)
            expandRemoteView.setViewVisibility(R.id.ll_bottomGridParent, View.VISIBLE)
        }
        val collapsedView = RemoteViews(context.applicationContext.packageName,
                R.layout.collapsed_notification_layout)
        setCollapseData(context, collapsedView, extras)
        builder.setSmallIcon(listener.getIcStatNotifyWhiteDrawable())
                .setLargeIcon(null)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandRemoteView)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setOngoing(false)
                .setWhen(`when`)
    }

    private fun setCollapseData(context: Context, remoteView: RemoteViews, extras: Bundle) {
        val notificationIntent = listener.getNotificationBroadcastIntent()
        notificationIntent.action = ACTION_GRID_CLICK
        val deepLink = extras.getString("gcm_webUrl")
        if (null != deepLink) {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, deepLink)
        } else {
            notificationIntent.putExtra(EXTRA_DEEP_LINK, "tokopedia://home")
        }
        notificationIntent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID)
        val contentIntent = getPendingIntent(context, 200, notificationIntent)
        remoteView.setTextViewText(R.id.tv_collapse_title, extras.getString("gcm_title"))
        remoteView.setTextViewText(R.id.tv_collapsed_message, extras.getString("gcm_alert"))
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, contentIntent)
    }


    private fun getPersistentClickPendingIntent(context: Context, iconUrl: String?,
                                                iconName: String?, deepLink: String?,
                                                campaignId: String?, requestCode: Int): PendingIntent {
        val intent = listener.getNotificationBroadcastIntent()
        intent.action = ACTION_PERSISTENT_CLICK
        intent.putExtra(EXTRA_CAMPAIGN_ID, campaignId)
        intent.putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID)
        intent.putExtra(EXTRA_PERSISTENT_ICON_URL, iconUrl)
        intent.putExtra(EXTRA_PERSISTENT_ICON_NAME, iconName)
        intent.putExtra(EXTRA_PERSISTENT_DEEPLINK, deepLink)
        return getPendingIntent(context, requestCode, intent)
    }

    private fun getPendingIntent(context: Context, requestCode: Int, intent: Intent): PendingIntent {
        return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createGrid(context: Context, remoteView: RemoteViews, resId: Int, url: String?, deepLink: String?, requestCode: Int) {
        if (null == url) return
        remoteView.setViewVisibility(resId, View.VISIBLE)
        remoteView.setImageViewBitmap(resId, MoEHelperUtils.downloadImageBitmap(url))
        val intent = listener.getNotificationBroadcastIntent()
        intent.action = ACTION_GRID_CLICK
        intent.putExtra(EXTRA_DEEP_LINK, deepLink)
        intent.putExtra(EXTRA_NOTIFICATION_ID, GRID_NOTIFICATION_ID)
        val resultPendingIntent = getPendingIntent(context, requestCode, intent)
        remoteView.setOnClickPendingIntent(resId, resultPendingIntent)
    }

    override fun onNotificationNotRequired(context: Context?, extras: Bundle?) {}

}