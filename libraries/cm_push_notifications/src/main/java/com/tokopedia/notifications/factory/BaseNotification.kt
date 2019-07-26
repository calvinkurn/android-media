package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.Notification.BADGE_ICON_SMALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationCacheHandler
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver
import org.json.JSONObject
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
abstract class BaseNotification internal constructor(protected var context: Context, var baseNotificationModel: BaseNotificationModel) {
    private var cacheHandler: CMNotificationCacheHandler? = null

    protected val builder: NotificationCompat.Builder
        get() {
            val builder: NotificationCompat.Builder = if (baseNotificationModel.channelName != null && !baseNotificationModel.channelName!!.isEmpty()) {
                NotificationCompat.Builder(context, baseNotificationModel.channelName!!)
            } else {
                NotificationCompat.Builder(context, CMConstant.NotificationChannel.CHANNEL_ID)
            }

            if (!TextUtils.isEmpty(baseNotificationModel.subText)) {
                builder.setSubText(baseNotificationModel.subText)
            }

            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            if (baseNotificationModel.isUpdateExisting) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    silentChannel()
                    builder.setChannelId(CMConstant.NotificationChannel.Channel_DefaultSilent_Id)
                } else {
                    builder.setSound(null)
                    builder.setVibrate(null)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                    builder.setBadgeIconType(BADGE_ICON_SMALL)
                    builder.setNumber(1)
                } else {
                    setNotificationSound(builder)
                    setNotificationPriorityPreOreo(builder, baseNotificationModel.priorityPreOreo)
                }
            }

            if (baseNotificationModel.icon!!.isEmpty()) {
                builder.setLargeIcon(bitmapLargeIcon)
            } else {
                builder.setLargeIcon(getBitmap(baseNotificationModel.icon))
            }
            builder.setSmallIcon(drawableIcon)
            return builder
        }

    protected val notificationBuilder: NotificationCompat.Builder
        get() {

            val builder: NotificationCompat.Builder = if (baseNotificationModel.channelName != null && !baseNotificationModel.channelName!!.isEmpty()) {
                NotificationCompat.Builder(context, baseNotificationModel.channelName!!)
            } else {
                NotificationCompat.Builder(context, CMConstant.NotificationChannel.CHANNEL_ID)
            }

            if (!TextUtils.isEmpty(baseNotificationModel.subText)) {
                builder.setSubText(baseNotificationModel.subText)
            }

            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            builder.setSmallIcon(drawableIcon)


            if (baseNotificationModel.isUpdateExisting) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    silentChannel()
                    builder.setChannelId(CMConstant.NotificationChannel.Channel_DefaultSilent_Id)
                } else {
                    builder.setSound(null)
                    builder.setVibrate(null)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                    builder.setBadgeIconType(BADGE_ICON_SMALL)
                    builder.setNumber(1)
                } else {
                    setNotificationSound(builder)
                    setNotificationPriorityPreOreo(builder, baseNotificationModel.priorityPreOreo)
                }
            }
            builder.setSmallIcon(drawableIcon)
            return builder
        }

    internal val drawableIcon: Int
        get() = if (GlobalConfig.isSellerApp())
            R.drawable.ic_status_bar_notif_sellerapp
        else
            R.drawable.ic_status_bar_notif_customerapp

    private val drawableLargeIcon: Int
        get() = if (GlobalConfig.isSellerApp())
            R.drawable.ic_big_notif_sellerapp
        else
            R.mipmap.ic_launcher

    protected val bitmapLargeIcon: Bitmap
        get() = BitmapFactory.decodeResource(context.resources, drawableLargeIcon)

    private val actionButtonHeightWidth: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.dp_20)


    private val imageWidth: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.cm_notif_width)

    private val imageHeight: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.cm_notif_height)

    private val vibratePattern: LongArray
        get() = longArrayOf(500, 500)

    private val ringtoneUri: Uri
        get() = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val requestCode: Int
        get() {
            if (cacheHandler == null)
                cacheHandler = CMNotificationCacheHandler(context)
            var requestCode = cacheHandler!!.getIntValue(CM_REQUEST_CODE)
            if (requestCode < 3000 || requestCode > 4000) {
                requestCode = 3000
            }
            cacheHandler!!.saveIntValue(CM_REQUEST_CODE, requestCode + 1)
            return requestCode
        }

    abstract fun createNotification(): Notification?

    private fun setNotificationPriorityPreOreo(builder: NotificationCompat.Builder, priority: Int) {
        builder.priority = priority
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun silentChannel() {
        val importance = NotificationManager.IMPORTANCE_MAX
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notificationChannel = NotificationChannel(CMConstant.NotificationChannel.Channel_DefaultSilent_Id,
                CMConstant.NotificationChannel.Channel_DefaultSilent_Name,
                importance)
        notificationChannel.description = CMConstant.NotificationChannel.Channel_DefaultSilent_DESCRIPTION
        notificationChannel.setSound(null, null)
        notificationChannel.enableLights(false)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        if (baseNotificationModel.channelName != null && !baseNotificationModel.channelName!!.isEmpty()) {
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(baseNotificationModel.channelName,
                    baseNotificationModel.channelName, importance)
            channel.description = CMConstant.NotificationChannel.CHANNEL_DESCRIPTION

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            if (baseNotificationModel.soundFileName != null && !baseNotificationModel.soundFileName!!.isEmpty()) {
                val att = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                channel.setSound(Uri.parse("android.resource://" + context.packageName + "/" +
                        "/raw/" + baseNotificationModel.soundFileName), att)
            }

            channel.vibrationPattern = vibratePattern

            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        } else {
            createDefaultChannel()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDefaultChannel() {
        val importance = NotificationManager.IMPORTANCE_MAX
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(CMConstant.NotificationChannel.CHANNEL_ID,
                CMConstant.NotificationChannel.CHANNEL,
                importance)
        val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        channel.setSound(ringtoneUri, att)
        channel.setShowBadge(true)
        channel.description = CMConstant.NotificationChannel.CHANNEL_DESCRIPTION
        channel.vibrationPattern = vibratePattern
        notificationManager.createNotificationChannel(channel)

    }

    private fun setNotificationSound(builder: NotificationCompat.Builder) {
        if (baseNotificationModel.soundFileName != null && !baseNotificationModel.soundFileName!!.isEmpty()) {
            val soundUri = Uri.parse("android.resource://" + context.packageName + "/" +
                    "/raw/" + baseNotificationModel.soundFileName)
            builder.setSound(soundUri)
        } else {
            builder.setSound(ringtoneUri)
        }
        builder.setVibrate(vibratePattern)
    }

    internal fun getBitmap(url: String?): Bitmap {
        return try {
            Glide.with(context).load(url)
                    .asBitmap()
                    .into(imageWidth, imageHeight)
                    .get(3, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: ExecutionException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: TimeoutException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: IllegalArgumentException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        }
    }

    internal fun getActionButtonBitmap(url: String): Bitmap {
        return try {
            val wh = actionButtonHeightWidth
            Glide.with(context).load(url)
                    .asBitmap()
                    .into(wh, wh)
                    .get(3, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: ExecutionException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: TimeoutException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        } catch (e: IllegalArgumentException) {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        }

    }

    internal fun createMainPendingIntent(baseNotificationModel: BaseNotificationModel, requestCode: Int): PendingIntent {
        var intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK
        intent.putExtras(getBundle(baseNotificationModel))
        intent = updateIntentWithCouponCode(baseNotificationModel, intent)
        return getPendingIntent(context, intent, requestCode)
    }

    internal fun createDismissPendingIntent(notificationId: Int, requestCode: Int): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }


    companion object {
        private const val CM_REQUEST_CODE = "cm_request_code"

        fun getBaseBroadcastIntent(context: Context, baseNotificationModel: BaseNotificationModel):
                Intent = Intent(context, CMBroadcastReceiver::class.java).apply {
            putExtra(CMConstant.EXTRA_BASE_MODEL, baseNotificationModel)
            putExtras(getBundle(baseNotificationModel))
            putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
            putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
        }

        /**
         * getBundle will return a Bundle
         * it bill create Bundle only if VideoPushModel or CustomValues
         *
         **/
        private fun getBundle(baseNotificationModel: BaseNotificationModel): Bundle {
            var bundle = Bundle()
            baseNotificationModel.videoPushModel?.let {
                bundle = jsonToBundle(bundle, baseNotificationModel.videoPushModel)
            }
            baseNotificationModel.customValues?.let {
                bundle = jsonToBundle(bundle, baseNotificationModel.videoPushModel)
            }
            return bundle
        }

        /**
         *
         *
         * */
        private fun jsonToBundle(bundle: Bundle, jsonObject: JSONObject?): Bundle {
            try {
                val iterator = jsonObject!!.keys()
                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                    val value = jsonObject.getString(key)
                    bundle.putString(key, value)
                }
            } catch (e: Exception) {

            }

            return bundle
        }

        fun getPendingIntent(context: Context, intent: Intent, requestCode: Int): PendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

        fun updateIntentWithCouponCode(baseNotificationModel: BaseNotificationModel, intent: Intent): Intent {
            if (baseNotificationModel.customValues != null)
                intent.putExtra(CMConstant.CouponCodeExtra.COUPON_CODE,
                        baseNotificationModel.customValues!!.optString(CMConstant.CustomValuesKeys.COUPON_CODE))
            return intent
        }
    }

}
