package com.tokopedia.bubbles.factory

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.tokopedia.bubbles.data.model.BubbleNotificationModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.bubbles.data.model.BubbleHistoryItemModel
import com.tokopedia.bubbles.utils.BubblesConst
import com.tokopedia.bubbles.utils.BubblesUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.orZero

class BubblesFactoryImpl(private val context: Context) : BubblesFactory {

    override fun setupBubble(
        builder: NotificationCompat.Builder,
        model: BubbleNotificationModel
    ) {
        val pendingIntentBundle =
            getPendingIntentBundle(model.notificationType, model.notificationId)
        val pendingIntent = getPendingIntent(model.applinks, pendingIntentBundle)
        val icon = createBubbleIcon(model.avatarUrl)
        val user = getBubblePerson(model.fullName)
        val person = getBubblePerson(icon, model.fullName)
        val messagingStyle = getMessagingStyle(user, person, model.summary, model.sentTime)
        val bubbleMetadata = getBubbleMetadata(pendingIntent, icon)

        builder
            .setBubbleMetadata(bubbleMetadata)
            .setLocusId(LocusIdCompat(model.messageId))
            .setShortcutId(model.messageId)
            .setCategory(Notification.CATEGORY_MESSAGE)
            .addPerson(person)
            .setStyle(messagingStyle)
            .setWhen(model.sentTime)
    }

    override fun updateShorcuts(historyModels: List<BubbleHistoryItemModel>) {
        val shortcuts = createShortcuts(historyModels)
        val maxShortcutCount = ShortcutManagerCompat.getMaxShortcutCountPerActivity(context)
        shortcuts.take(maxShortcutCount).forEach { shortcut ->
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
            putBoolean(BubblesConst.EXTRA_APPLINK_FROM_PUSH, true);
            putInt(BubblesConst.EXTRA_NOTIFICATION_TYPE, notificationType);
            putInt(BubblesConst.EXTRA_NOTIFICATION_ID, notificationId);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(
        applinks: String,
        bundle: Bundle
    ): PendingIntent {
        val bubbleChatIntent = createBubbleChatIntent(applinks, bundle)
        return PendingIntent.getActivity(
            context,
            REQUEST_BUBBLE,
            bubbleChatIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
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

    private fun getBubblePerson(fullName: String): Person {
        return Person.Builder()
            .setName(fullName)
            .build()
    }

    private fun getBubblePerson(
        icon: IconCompat,
        fullName: String
    ): Person {
        return Person.Builder()
            .setName(fullName)
            .setIcon(icon)
            .build()
    }

    private fun createBubbleIcon(avatarUrl: String): IconCompat {
        val bitmap = BubblesUtils.getBitmap(context, avatarUrl, getBitmapWidth(), getBitmapHeight())
        return IconCompat.createWithAdaptiveBitmap(bitmap)
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
        icon: IconCompat
    ): NotificationCompat.BubbleMetadata {
        return NotificationCompat.BubbleMetadata.Builder(
            pendingIntent,
            icon
        ).setDesiredHeight(getBubbleHeight())
            .build()
    }

    private fun createShortcuts(historyModels: List<BubbleHistoryItemModel>): List<ShortcutInfoCompat> {
        return historyModels.map {
            ShortcutInfoCompat.Builder(context, it.messageId)
                .setLocusId(LocusIdCompat(it.messageId))
                .setActivity(
                    ComponentName(
                        context.packageName,
                        GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME
                    )
                )
                .setShortLabel(it.senderName)
                .setIcon(createBubbleIcon(it.avatarUrl))
                .setLongLived(true)
                .setIntent(createBubbleChatIntent(it.applink))
                .setPerson(getBubblePerson(createBubbleIcon(it.avatarUrl), it.senderName))
                .build()
        }
    }

    companion object {
        const val REQUEST_BUBBLE = 2
    }

}
