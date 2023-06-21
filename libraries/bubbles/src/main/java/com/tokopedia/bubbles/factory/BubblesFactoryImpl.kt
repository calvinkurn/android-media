package com.tokopedia.bubbles.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.applink.RouteManager
import com.tokopedia.bubbles.analytics.BubblesTracker
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.data.model.BubbleNotificationModel
import com.tokopedia.bubbles.utils.BubblesConst
import com.tokopedia.bubbles.utils.BubblesUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class BubblesFactoryImpl(private val context: Context) : BubblesFactory {

    private val userSession: UserSessionInterface = UserSession(context)

    override fun setupBubble(
        builder: NotificationCompat.Builder,
        model: BubbleNotificationModel,
        bubbleBitmap: Bitmap?
    ) {
        val icon = createBubbleIcon(model.avatarUrl, bubbleBitmap)
        if (icon != null) {
            val pendingIntentBundle =
                getPendingIntentBundle(model.notificationType, model.notificationId)
            val pendingIntent = getPendingIntent(model.applinks, pendingIntentBundle)
            val user = getBubblePerson(model.fullName)
            val person = getBubblePerson(icon, model.fullName)
            val messagingStyle = getMessagingStyle(user, person, model.summary, model.sentTime)
            val bubbleMetadata = getBubbleMetadata(pendingIntent, icon, model.isFromUser)

            sendImpressionTracking(model)

            builder
                .setBubbleMetadata(bubbleMetadata)
                .setLocusId(LocusIdCompat(model.shortcutId))
                .setShortcutId(model.shortcutId)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .addPerson(person)
                .setStyle(messagingStyle)
                .setWhen(model.sentTime)
        }
    }

    private fun sendImpressionTracking(model: BubbleNotificationModel) {
        try {
            val uri = Uri.parse(model.applinks)
            when (uri.host) {
                TOPCHAT_HOST -> {
                    BubblesTracker.sendImpressionTracking(
                        userSession.shopId,
                        model.shortcutId,
                        model.senderId
                    )
                }
                else -> {
                    // no op
                }
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    override fun updateShorcuts(historyModels: List<BubbleHistoryItemModel>, bubbleModel: BubbleNotificationModel, bubbleBitmap: Bitmap?) {
        var shortcuts = createShortcuts(historyModels, bubbleBitmap)

        shortcuts = shortcuts.sortedByDescending { it.id == bubbleModel.shortcutId }

        val maxShortcutCount = ShortcutManagerCompat.getMaxShortcutCountPerActivity(context)

        if (shortcuts.size > maxShortcutCount) {
            shortcuts = shortcuts.take(maxShortcutCount)
        }

        shortcuts.forEach { shortcut ->
            ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
        }
    }

    override fun getBitmapWidth(): Int {
        return context.resources.getDimensionPixelSize(com.tokopedia.bubbles.R.dimen.bubble_image_width)
            .orZero()
    }

    override fun getBitmapHeight(): Int {
        return context.resources.getDimensionPixelSize(com.tokopedia.bubbles.R.dimen.bubble_image_height)
            .orZero()
    }

    override fun getBubbleHeight(): Int {
        return context.resources.getDimensionPixelSize(com.tokopedia.bubbles.R.dimen.bubble_height)
            .orZero()
    }

    private fun getPendingIntentBundle(
        notificationType: Int,
        notificationId: Int
    ): Bundle {
        return Bundle().apply {
            putBoolean(BubblesConst.EXTRA_APPLINK_FROM_PUSH, true)
            putInt(BubblesConst.EXTRA_NOTIFICATION_TYPE, notificationType)
            putInt(BubblesConst.EXTRA_NOTIFICATION_ID, notificationId)
            putString(BubblesConst.EXTRA_IS_FROM_BUBBLE, BubblesConst.EXTRA_BUBBLE_SOURCE)
        }
    }

    private fun getPendingIntent(
        applinks: String,
        bundle: Bundle
    ): PendingIntent {
        val bubbleChatIntent = createBubbleChatIntent(applinks, bundle)
        return PendingIntent.getActivity(
            context,
            REQUEST_BUBBLE,
            bubbleChatIntent,
            flagUpdateCurrent()
        )
    }

    private fun flagUpdateCurrent(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    private fun createBubbleChatIntent(
        applinks: String,
        bundle: Bundle? = null
    ): Intent {
        return RouteManager.getIntent(context, applinks).apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(applinks)
            bundle?.let {
                putExtras(it)
            }
        }
    }

    private fun getComponentName(): String {
        return GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME
    }

    private fun getBubblePerson(fullName: String): Person {
        return Person.Builder()
            .setName(fullName)
            .build()
    }

    private fun getBubblePerson(
        icon: IconCompat?,
        fullName: String
    ): Person {
        return Person.Builder()
            .setName(fullName)
            .setIcon(icon)
            .build()
    }

    private fun createBubbleIcon(avatarUrl: String, bubbleBitmap: Bitmap?): IconCompat? {
        val bitmap = bubbleBitmap ?: BubblesUtils.getBitmap(context, avatarUrl, getBitmapWidth(), getBitmapHeight())
        return bitmap?.let {
            IconCompat.createWithAdaptiveBitmap(it)
        }
    }

    private fun getMessagingStyle(
        user: Person,
        person: Person,
        summary: String,
        sentTime: Long
    ): NotificationCompat.MessagingStyle {
        return NotificationCompat.MessagingStyle(user).also {
            val message = NotificationCompat.MessagingStyle.Message(
                summary,
                sentTime,
                person
            )
            it.addMessage(message)
        }
    }

    private fun getBubbleMetadata(
        pendingIntent: PendingIntent,
        icon: IconCompat,
        isFromUser: Boolean
    ): NotificationCompat.BubbleMetadata {
        return NotificationCompat.BubbleMetadata.Builder(
            pendingIntent,
            icon
        ).setDesiredHeight(getBubbleHeight()).apply {
            if (isFromUser) {
                setAutoExpandBubble(true)
                setSuppressNotification(true)
            }
        }.build()
    }

    private fun createShortcuts(historyModels: List<BubbleHistoryItemModel>, bubbleBitmap: Bitmap?): List<ShortcutInfoCompat> {
        return historyModels.map {
            ShortcutInfoCompat.Builder(context, it.messageId)
                .setLocusId(LocusIdCompat(it.messageId))
                .setActivity(
                    ComponentName(
                        context.packageName,
                        getComponentName()
                    )
                )
                .setShortLabel(it.senderName)
                .setIcon(createBubbleIcon(it.avatarUrl, bubbleBitmap))
                .setLongLived(true)
                .setIntent(createBubbleChatIntent(it.applink))
                .setPerson(getBubblePerson(createBubbleIcon(it.avatarUrl, bubbleBitmap), it.senderName))
                .build()
        }
    }

    companion object {
        const val REQUEST_BUBBLE = 2
        const val TOPCHAT_HOST = "topchat"
    }
}
