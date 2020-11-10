package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.PersistentButton

/**
 * @author lalit.singh
 */
class PersistentNotification internal constructor(
        context: Context,
        baseNotificationModel: BaseNotificationModel
) : BaseNotification(context, baseNotificationModel) {

    private val titleResIds = arrayOf(R.id.title1, R.id.title2, R.id.title3, R.id.title4)
    private val imageViewResIds = arrayOf(R.id.image_icon1, R.id.image_icon2, R.id.image_icon3, R.id.image_icon4)
    private val containerResIds = arrayOf(R.id.lin_container_1, R.id.lin_container_2, R.id.lin_container_3, R.id.lin_container_4)

    override fun createNotification(): Notification? {
        if (baseNotificationModel.persistentButtonList == null || baseNotificationModel.persistentButtonList?.isEmpty() == true) {
            return null
        }

        val remoteViews = getPersistentRemoteView()

        return builder
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setContentTitle(baseNotificationModel.title)
                .setSmallIcon(drawableIcon)
                .setContentIntent(getBlankContentClickPendingIntent())
                .setAutoCancel(false)
                .setOngoing(true)
                .build()
    }

    private fun getPersistentRemoteView(): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.cm_persistent_notification_layout)
        val persistentButtonList = baseNotificationModel.persistentButtonList

        // close button of persistent notification
        remoteView.setOnClickPendingIntent(R.id.image_icon5, getPersistentClosePendingIntent())

        remoteView.setImageViewResource(R.id.image_icon5, if (isDarkMode()) {
            R.drawable.cm_ic_btn_close_white
        } else {
            R.drawable.cm_ic_btn_close_black
        })

        // list of persistent button
        persistentButtonList?.let {
            if (it.isNotEmpty())
                for (i in 0 until it.size) {
                    val persistentButton = it[i]
                    remoteView.setViewVisibility(containerResIds[i], View.VISIBLE)
                    remoteView.setTextViewText(titleResIds[i], persistentButton.text)
                    remoteView.setImageViewBitmap(imageViewResIds[i], getBitmap(persistentButton.icon))
                    remoteView.setOnClickPendingIntent(containerResIds[i], getPersistentClickPendingIntent(persistentButton))
                }

            remoteView.setOnClickPendingIntent(R.id.image_icon6,
                    getPersistentClickPendingIntent(PersistentButton().apply {
                        appLink = baseNotificationModel.appLink
                        isAppLogo = true
                    })
            )
        }

        return remoteView
    }

    private fun isDarkMode(): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    private fun getPersistentClickPendingIntent(persistentButton: PersistentButton): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK
        intent.putExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA, persistentButton)
        return getPendingIntent(context, intent, requestCode)
    }

    private fun getPersistentClosePendingIntent(): PendingIntent {
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
