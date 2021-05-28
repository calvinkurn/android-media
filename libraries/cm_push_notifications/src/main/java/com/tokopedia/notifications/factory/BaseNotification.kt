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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
const val IMAGE_DOWNLOAD_TIME_OUT_SECOND = 10L

interface BaseNotificationContract {
    fun defaultIcon(): Bitmap
    fun getBitmap(url: String?): Bitmap
    fun loadResourceAsBitmap(resId: Int, result: (Bitmap) -> Unit)
}

abstract class BaseNotification internal constructor(
        protected var context: Context,
        var baseNotificationModel: BaseNotificationModel
): BaseNotificationContract {

    private var cacheHandler: CMNotificationCacheHandler? = null
    val NOTIFICATION_NUMBER = 1

    protected val builder: NotificationCompat.Builder
        get() {
            val builder: NotificationCompat.Builder =
                    if (baseNotificationModel.channelName != null && baseNotificationModel.channelName!!.isNotEmpty()) {
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
                    builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    builder.setNumber(NOTIFICATION_NUMBER)
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
                    builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
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
            com.tokopedia.notification.common.R.mipmap.ic_statusbar_notif_seller
        else
            R.mipmap.ic_statusbar_notif_customer

    private val drawableLargeIcon: Int
        get() = if (GlobalConfig.isSellerApp())
            com.tokopedia.resources.common.R.mipmap.ic_launcher_sellerapp
        else
            com.tokopedia.resources.common.R.mipmap.ic_launcher_customerapp
    internal val bitmapLargeIcon: Bitmap
        get() = createBitmap()

    override fun defaultIcon(): Bitmap {
        return bitmapLargeIcon
    }

    private fun createBitmap(): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val drawable = context.resources.getDrawable(drawableLargeIcon)
            val bmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bmp
        } else {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        }
    }

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
        val importance = NotificationManager.IMPORTANCE_HIGH
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
            val importance = NotificationManager.IMPORTANCE_HIGH
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
        val importance = NotificationManager.IMPORTANCE_HIGH
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

    override fun getBitmap(url: String?): Bitmap {
        return try {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(imageWidth, imageHeight)
                    .get(IMAGE_DOWNLOAD_TIME_OUT_SECOND, TimeUnit.SECONDS)
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

    internal fun getBitmap(url: String?, width: Int, height: Int): Bitmap {
        return try {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(width, height)
                    .get(IMAGE_DOWNLOAD_TIME_OUT_SECOND, TimeUnit.SECONDS)
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

    override fun loadResourceAsBitmap(resId: Int, result: (Bitmap) -> Unit) {
        Glide.with(context)
                .asBitmap()
                .load(resId)
                .placeholder(resId)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        result(resource)
                    }
                })
    }

    internal fun getActionButtonBitmap(url: String): Bitmap {
        return try {
            val wh = actionButtonHeightWidth
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(wh, wh)
                    .get(IMAGE_DOWNLOAD_TIME_OUT_SECOND, TimeUnit.SECONDS)
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

    internal fun createMainPendingIntent(baseNotificationModel: BaseNotificationModel, reqCode: Int): PendingIntent {
        var intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK
        intent.putExtras(getBundle(baseNotificationModel))
        intent = updateIntentWithCouponCode(baseNotificationModel, intent)
        return getPendingIntent(context, intent, reqCode)
    }

    internal fun createDismissPendingIntent(notificationId: Int, requestCode: Int): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }


    companion object {
        private const val CM_REQUEST_CODE = "cm_request_code"

        fun getBaseBroadcastIntent(context: Context, baseNotificationModel: BaseNotificationModel): Intent {
            val intent = Intent(context, CMBroadcastReceiver::class.java)
            intent.putExtra(CMConstant.EXTRA_BASE_MODEL,baseNotificationModel)
            intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
            intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
            intent.putExtras(getBundle(baseNotificationModel))
            return intent
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
                if (it.isNotEmpty())
                    bundle = jsonToBundle(bundle, JSONObject(it))
            }
            return bundle
        }

        /**
         *
         *
         * */
        private fun jsonToBundle(bundle: Bundle, jsonObject: JSONObject?): Bundle {
            jsonObject?.let {
                val iterator = it.keys()
                while (iterator.hasNext()) {
                    val key = iterator.next() as String
                    val value = it.getString(key)
                    bundle.putString(key, value)
                }
            }
            return bundle
        }

        fun getPendingIntent(context: Context, intent: Intent, reqCode: Int): PendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        reqCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

        fun updateIntentWithCouponCode(baseNotificationModel: BaseNotificationModel, intent: Intent): Intent {
            baseNotificationModel.customValues?.let {
                if (it.isNotEmpty()) {
                    val couponCode = (JSONObject(it)).optString(CMConstant.CustomValuesKeys.COUPON_CODE)
                    val gratificationId = (JSONObject(it)).optString(CMConstant.CustomValuesKeys.GRATIFICATION_ID)
                    if (!couponCode.isNullOrEmpty()) {
                        intent.putExtra(CMConstant.CouponCodeExtra.COUPON_CODE, couponCode)
                    }
                    if (!gratificationId.isNullOrEmpty()) {
                        intent.putExtra(CMConstant.CouponCodeExtra.GRATIFICATION_ID, gratificationId)
                    }
                }
            }
            return intent
        }
    }

}
