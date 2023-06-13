package com.tokopedia.notifications.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.CMNotificationCacheHandler
import com.tokopedia.notifications.factory.helper.NotificationChannelController
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.receiver.CMBroadcastReceiver
import com.tokopedia.notifications.receiver.CMReceiverActivity
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
) : BaseNotificationContract {

    private val notificationChannelController by lazy {
        NotificationChannelController.getNotificationController(context = context)
    }

    private val cacheHandler: CMNotificationCacheHandler by lazy {
        CMNotificationCacheHandler(context)
    }

    private val NOTIFICATION_NUMBER = 1


    abstract fun createNotification(): Notification?

    protected val builder: NotificationCompat.Builder
        get() {
            val channelID = notificationChannelController.getChannelID(
                baseNotificationModel.channelName,
                baseNotificationModel.soundFileName
            )

            val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                context, channelID
            )

            if (!TextUtils.isEmpty(baseNotificationModel.subText)) {
                builder.setSubText(baseNotificationModel.subText)
            }

            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            builder.setSmallIcon(drawableIcon)
            setGroup(builder)
            setSoundProperties(builder)
            setNotificationIcon(builder)
            return builder
        }

    protected val noLargeIconNotificationBuilder: NotificationCompat.Builder
        get() {
            val channelID = notificationChannelController.getChannelID(
                baseNotificationModel.channelName,
                baseNotificationModel.soundFileName
            )

            val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                context, channelID
            )

            if (!TextUtils.isEmpty(baseNotificationModel.subText)) {
                builder.setSubText(baseNotificationModel.subText)
            }

            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            builder.setSmallIcon(drawableIcon)
            setGroup(builder)
            setSoundProperties(builder)
            return builder
        }

     internal val summaryNotificationBuilder: Notification?
        get() {
            val channelID = notificationChannelController.getChannelID(
                baseNotificationModel.channelName,
                baseNotificationModel.soundFileName
            )
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                context, channelID
            )
            builder.setSmallIcon(drawableIcon)
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            builder.setStyle(NotificationCompat.InboxStyle()
                .setSummaryText(baseNotificationModel.subText))
            setGroup(builder)
            builder.setGroupSummary(true)
            setSoundProperties(builder)
            return builder.build()
        }

    private fun setGroup(builder: NotificationCompat.Builder) {
        baseNotificationModel.groupId.let { id ->
            if(id.toString().isNotBlank() && id != 0) {
                val groupKey = id.toString()
                builder.setGroup(groupKey)
            }
        }

    }

    /*
    *
    * 1. This function is used to set silent notification properties
    * 2. Setting sound for PreOreo
    * 3. Setting PreOreo Notification Priority
    * 4. Setting Badge Icon and Notification count for OS above Marshmallow
    *
    * */
    private fun setSoundProperties(builder: NotificationCompat.Builder) {
        if (baseNotificationModel.isUpdateExisting) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val silentChannelId = notificationChannelController.createAndGetSilentChannel()
                builder.setChannelId(silentChannelId)
            } else {
                notificationChannelController.setPreOreoSilentSound(builder)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                builder.setNumber(NOTIFICATION_NUMBER)
            } else {
                notificationChannelController.setPreOreoSound(
                    builder,
                    baseNotificationModel.soundFileName
                )
                builder.priority = baseNotificationModel.priorityPreOreo
            }
        }
    }


    /**
     * 1. Setting Large icon to default Notification style
     * 2. Setting Small icon to notification
     * */
    private fun setNotificationIcon(builder: NotificationCompat.Builder) {
        if (baseNotificationModel.icon!!.isEmpty()) {
            builder.setLargeIcon(bitmapLargeIcon)
        } else {
            builder.setLargeIcon(getBitmap(baseNotificationModel.icon))
        }
    }

    internal val drawableIcon: Int
        get() = if (GlobalConfig.isSellerApp())
            com.tokopedia.notification.common.R.mipmap.ic_statusbar_notif_seller
        else
            R.mipmap.ic_statusbar_notif_customer

    private val drawableLargeIcon: Int
        get() = GlobalConfig.LAUNCHER_ICON_RES_ID

    internal val bitmapLargeIcon: Bitmap
        get() = createBitmap()

    fun getApplicationName(context: Context): String {
        return context.getString(context.applicationInfo.labelRes)
    }

    override fun defaultIcon(): Bitmap {
        return bitmapLargeIcon
    }

    private fun createBitmap(): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val drawable = context.resources.getDrawable(drawableLargeIcon)
            val bmp = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bmp)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bmp
        } else {
            BitmapFactory.decodeResource(context.resources, drawableLargeIcon)
        }
    }

    private val imageWidth: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.cm_notif_width)

    private val imageHeight: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.cm_notif_height)

    val requestCode: Int
        get() {
            var requestCode = cacheHandler.getIntValue(CM_REQUEST_CODE)
            if (requestCode < 3000 || requestCode > 4000) {
                requestCode = 3000
            }
            cacheHandler.saveIntValue(CM_REQUEST_CODE, requestCode + 1)
            return requestCode
        }


    fun loadBitmap(url: String?): Bitmap? {
        if (url.isNullOrBlank())
            return null
        return try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(imageWidth, imageHeight)
                .get(IMAGE_DOWNLOAD_TIME_OUT_SECOND, TimeUnit.SECONDS)
        } catch (e: Exception) {
            null
        }
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

    internal fun createMainPendingIntent(
        baseNotificationModel: BaseNotificationModel, reqCode: Int
    ): PendingIntent {
        var intent = getBaseBroadcastIntent(context, baseNotificationModel)
        intent.action = CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK
        intent = updateIntentWithCouponCode(baseNotificationModel, intent)
        return getPendingIntent(context, intent, reqCode)
    }

    internal fun createDismissPendingIntent(notificationId: Int, requestCode: Int): PendingIntent {
        val intent = getBaseBroadcastIntent(context, baseNotificationModel, true)
        intent.action = CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS
        return getPendingIntent(context, intent, requestCode)
    }


    companion object {
        private const val CM_REQUEST_CODE = "cm_request_code"
        private const val sdkLevel31 = 31
        private const val ACTIVITY_RECEIVER = "CMReceiverActivity"
        fun getBaseBroadcastIntent(
            context: Context,
            baseNotificationModel: BaseNotificationModel,
            isDismissIntent: Boolean = false
        ): Intent {

            val intent = if (Build.VERSION.SDK_INT >= sdkLevel31 && !isDismissIntent) {
               Intent(context, CMReceiverActivity::class.java).apply {
                   flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
               }
            } else {
                Intent(context, CMBroadcastReceiver::class.java)
            }
            intent.putExtra(CMConstant.EXTRA_BASE_MODEL, baseNotificationModel)
            intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.notificationId)
            intent.putExtra(CMConstant.EXTRA_CAMPAIGN_ID, baseNotificationModel.campaignId)
            return intent
        }

        fun getPendingIntent(context: Context, intent: Intent, reqCode: Int): PendingIntent =
            if (Build.VERSION.SDK_INT >= sdkLevel31 && intent.component?.className?.contains(
                    ACTIVITY_RECEIVER) == true) {
                PendingIntent.getActivity(
                    context,
                    reqCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else if (Build.VERSION.SDK_INT >= sdkLevel31 && intent.component?.className?.contains(
                    ACTIVITY_RECEIVER) == false) {
                PendingIntent.getBroadcast(
                    context,
                    reqCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            else {
                PendingIntent.getBroadcast(
                    context,
                    reqCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        fun updateIntentWithCouponCode(
            baseNotificationModel: BaseNotificationModel,
            intent: Intent
        ): Intent {
            baseNotificationModel.customValues?.let {
                if (it.isNotEmpty()) {
                    val couponCode =
                        (JSONObject(it)).optString(CMConstant.CustomValuesKeys.COUPON_CODE)
                    val gratificationId =
                        (JSONObject(it)).optString(CMConstant.CustomValuesKeys.GRATIFICATION_ID)
                    if (!couponCode.isNullOrEmpty()) {
                        intent.putExtra(CMConstant.CouponCodeExtra.COUPON_CODE, couponCode)
                    }
                    if (!gratificationId.isNullOrEmpty()) {
                        intent.putExtra(
                            CMConstant.CouponCodeExtra.GRATIFICATION_ID,
                            gratificationId
                        )
                    }
                }
            }
            return intent
        }
    }

}
