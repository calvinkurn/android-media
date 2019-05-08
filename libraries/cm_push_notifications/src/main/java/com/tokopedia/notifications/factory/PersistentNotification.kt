package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.PersistentButton
import com.tokopedia.notifications.receiver.CMBroadcastReceiver

/**
 * @author lalit.singh
 */
class PersistentNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel) : BaseNotification(context, baseNotificationModel) {

    /*
     * create RemoteViews using BaseNotificationModel
     *
     * */
    private val persistentRemoteView: RemoteViews
        get() {
            val remoteView = RemoteViews(context.packageName, R.layout.persistent_notification_layout)
            remoteView.setOnClickPendingIntent(R.id.image_icon5, persistentClosePIntent)
            val persistentButtonList = baseNotificationModel.persistentButtonList

            persistentButtonList?.let {
                val listSize = persistentButtonList?.size
                var persistentButton: PersistentButton
                if (listSize > 0) {
                    persistentButton = persistentButtonList[0]
                    remoteView.setTextViewText(R.id.title1, persistentButton.text)
                    remoteView.setImageViewBitmap(R.id.image_icon1, getBitmap(persistentButton.icon))
                    remoteView.setOnClickPendingIntent(R.id.lin_container_1, getPersistentClickPendingIntent(persistentButton, requestCode))
                }
                if (listSize > 1) {
                    remoteView.setViewVisibility(R.id.lin_container_2, View.VISIBLE)
                    persistentButton = persistentButtonList[1]
                    remoteView.setTextViewText(R.id.title2, persistentButton.text)
                    remoteView.setImageViewBitmap(R.id.image_icon2, getBitmap(persistentButton.icon))
                    remoteView.setOnClickPendingIntent(R.id.lin_container_2, getPersistentClickPendingIntent(persistentButton, requestCode))
                }
                if (listSize > 2) {
                    remoteView.setViewVisibility(R.id.lin_container_3, View.VISIBLE)
                    persistentButton = persistentButtonList[2]
                    remoteView.setTextViewText(R.id.title3, persistentButton.text)
                    remoteView.setImageViewBitmap(R.id.image_icon3, getBitmap(persistentButton.icon))

                    remoteView.setOnClickPendingIntent(R.id.lin_container_3, getPersistentClickPendingIntent(persistentButton, requestCode))
                }
                if (listSize > 3) {
                    remoteView.setViewVisibility(R.id.lin_container_4, View.VISIBLE)
                    persistentButton = persistentButtonList[3]
                    remoteView.setTextViewText(R.id.title4, persistentButton.text)
                    remoteView.setImageViewBitmap(R.id.image_icon4, getBitmap(persistentButton.icon))
                    remoteView.setOnClickPendingIntent(R.id.lin_container_4, getPersistentClickPendingIntent(persistentButton, requestCode))
                }


                persistentButton = PersistentButton()
                persistentButton.appLink = baseNotificationModel.appLink
                persistentButton.isAppLogo = true
                remoteView.setOnClickPendingIntent(R.id.image_icon6, getPersistentClickPendingIntent(persistentButton, requestCode))


            }

            return remoteView
        }

    private val persistentClosePIntent: PendingIntent
        get() {
            val intent = Intent(context, CMBroadcastReceiver::class.java)
            intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
            intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
            intent.action = CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                PendingIntent.getBroadcast(
                        context,
                        requestCode, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
            } else {
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }

    override fun createNotification(): Notification? {
        val builder = builder
        builder.setContentTitle(baseNotificationModel.title)
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(getPersistentClickPendingIntent(requestCode))
        builder.setAutoCancel(false)
        builder.setOngoing(true)
        if (baseNotificationModel.persistentButtonList == null || baseNotificationModel.persistentButtonList?.size == 0)
            return null
        val remoteViews = persistentRemoteView
        builder.setCustomContentView(remoteViews)
        builder.setCustomBigContentView(remoteViews)
        return builder.build()
    }

    private fun getPersistentClickPendingIntent(persistentButton: PersistentButton, requestCode: Int): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA, persistentButton)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun getPersistentClickPendingIntent(requestCode: Int): PendingIntent {
        val intent = Intent(context, CMBroadcastReceiver::class.java)
        intent.action = CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
        intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK, baseNotificationModel.appLink)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}
