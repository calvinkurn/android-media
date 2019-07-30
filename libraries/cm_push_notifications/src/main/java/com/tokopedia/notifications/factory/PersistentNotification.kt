package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.PersistentButton

/**
 * @author lalit.singh
 */
class PersistentNotification internal constructor(context: Context, baseNotificationModel: BaseNotificationModel)
    : BaseNotification(context, baseNotificationModel) {

    /*
     * create RemoteViews using BaseNotificationModel
     *
     * */
    private val titleResIds = arrayOf(R.id.title1, R.id.title2, R.id.title3, R.id.title4)
    private val imageViewResIds = arrayOf(R.id.image_icon1, R.id.image_icon2, R.id.image_icon3, R.id.image_icon4)
    private val containerResIds = arrayOf(R.id.lin_container_1, R.id.lin_container_2, R.id.lin_container_3, R.id.lin_container_4)


    override fun createNotification(): Notification? {
        val builder = builder
        builder.setContentTitle(baseNotificationModel.title)
        builder.setSmallIcon(drawableIcon)
        builder.setContentIntent(getBlankContentClickPendingIntent())
        builder.setAutoCancel(false)
        builder.setOngoing(true)
        if (baseNotificationModel.persistentButtonList == null || baseNotificationModel.persistentButtonList?.size == 0)
            return null
        val remoteViews = getPersistentRemoteView()
        builder.setCustomContentView(remoteViews)
        builder.setCustomBigContentView(remoteViews)
        return builder.build()
    }

    private fun getPersistentRemoteView() : RemoteViews{
        val remoteView = RemoteViews(context.packageName, R.layout.cm_persistent_notification_layout)
        remoteView.setOnClickPendingIntent(R.id.image_icon5, getPersistentClosePendingIntent())
        val persistentButtonList = baseNotificationModel.persistentButtonList
        persistentButtonList?.let {
            val listSize = it?.size
            if (listSize != 0)
                for (i in 0 until listSize) {
                    val persistentButton = it[i]
                    remoteView.setViewVisibility(containerResIds[i], View.VISIBLE)
                    remoteView.setTextViewText(titleResIds[i], persistentButton.text)
                    remoteView.setImageViewBitmap(imageViewResIds[i], getBitmap(persistentButton.icon))
                    remoteView.setOnClickPendingIntent(containerResIds[i], getPersistentClickPendingIntent(persistentButton))
                }

            val persistentButton = PersistentButton()
            persistentButton.appLink = baseNotificationModel.appLink
            persistentButton.isAppLogo = true
            remoteView.setOnClickPendingIntent(R.id.image_icon6, getPersistentClickPendingIntent(persistentButton))
        }
        return remoteView
    }


    private fun getPersistentClickPendingIntent(persistentButton: PersistentButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK
        intent.putExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA, persistentButton)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getPersistentClosePendingIntent() : PendingIntent{
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getBlankContentClickPendingIntent(): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_NOTIFICATION_BLANK
        return getPendingIntent(context, intent, requestCode)
    }
}
