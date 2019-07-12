package com.tokopedia.notifications.receiver

import android.app.Activity
import android.app.NotificationManager
import android.content.*
import android.support.v4.app.NotificationManagerCompat
import android.text.TextUtils
import android.widget.Toast
import com.tokopedia.applink.RouteManager
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.factory.BaseNotification
import com.tokopedia.notifications.factory.CMNotificationFactory
import com.tokopedia.notifications.factory.CarouselNotification
import com.tokopedia.notifications.factory.ProductNotification
import com.tokopedia.notifications.model.*
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import kotlin.coroutines.CoroutineContext


/**
 * @author lalit.singh
 */
class CMBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val action = intent.action
            if (!intent.hasExtra(CMConstant.EXTRA_NOTIFICATION_ID))
                return
            val notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0)
            val campaignId = intent.getIntExtra(CMConstant.EXTRA_CAMPAIGN_ID, 0)
            if (null != action) {
                when (action) {
                    CMConstant.ReceiverAction.ACTION_BUTTON -> {
                        handleActionButtonClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK -> {
                        handlePersistentClick(context, intent)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT -> {
                        cancelPersistentNotification(context, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS -> {
                        NotificationManagerCompat.from(context).cancel(notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK -> {
                        handleNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }

                    CMConstant.ReceiverAction.ACTION_GRID_CLICK -> {
                        handleGridNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }

                    CMConstant.ReceiverAction.ACTION_CAROUSEL_MAIN_CLICK-> {
                        handleCarouselMainClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK -> {
                        handleCarouselImageClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK ->
                        CarouselNotification.onRightIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK ->
                        CarouselNotification.onLeftIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_NOTIFICATION_DISMISS -> {
                        clearCarouselImages(context.applicationContext)
                    }


                    CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK -> {
                        handleProductClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK -> {
                        handleCollapsedViewClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_LEFT_CLICK -> {
                        ProductNotification.onLeftIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK -> {
                        ProductNotification.onRightIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS -> {
                        clearProductImages(context.applicationContext)
                    }

                }
            }
        } catch (e: Exception) {
        }
    }

    private fun handleCarouselMainClick(context: Context, intent: Intent, notificationId: Int) {
        val baseNotificationModel: BaseNotificationModel = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, baseNotificationModel.appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
        clearCarouselImages(context.applicationContext)
    }

    private fun handleProductClick(context: Context, intent: Intent, notificationId: Int) {
        val productInfo: ProductInfo = intent.getParcelableExtra(CMConstant.EXTRA_PRODUCT_INFO)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, productInfo.appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
        clearProductImages(context.applicationContext)
    }

    private fun handleCollapsedViewClick(context: Context, intent: Intent, notificationId: Int) {
        val baseNotificationModel: BaseNotificationModel = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, baseNotificationModel.appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
        clearProductImages(context.applicationContext)
    }

    private fun clearProductImages(context: Context) {
        launchCatchError(block = {
            CarouselUtilities.deleteProductImageDirectory(context.applicationContext)
        },
                onError = {
                    Timber.i(it)
                })
    }

    private fun clearCarouselImages(context: Context) {
        launchCatchError(block = {
            CarouselUtilities.deleteCarouselImageDirectory(context.applicationContext)
        },
                onError = {
                    Timber.i(it)
                })
    }

    private fun handleGridNotificationClick(context: Context, intent: Intent, notificationId: Int) {
        val appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, appLinks)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun cancelPersistentNotification(context: Context, notificationId: Int) {
        CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                PersistentEvent.EVENT_ACTION_CANCELED, PersistentEvent.EVENT_LABEL)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handlePersistentClick(context: Context, intent: Intent) {
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        if (intent.hasExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)) {
            val persistentButton = intent.getParcelableExtra<PersistentButton>(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)
                    ?: return
            if (persistentButton.isAppLogo) {
                CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                        PersistentEvent.EVENT_ACTION_LOGO_CLICK, persistentButton.appLink!!)
            } else {
                CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                        persistentButton.text!!, persistentButton.appLink!!)
            }
            val appLinkIntent = RouteManager.getIntent(context.applicationContext, persistentButton.appLink)
            appLinkIntent.putExtras(intent.extras!!)
            startActivity(context, appLinkIntent)
        }
    }

    private fun copyToClipboard(context: Context, contents: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", contents)
        clipboard.primaryClip = clip
        applyPromoCode(context, contents)
        Toast.makeText(context, "${context.getString(R.string.cm_tv_coupon_code_copied)} $contents"
                , Toast.LENGTH_LONG).show()
    }

    private fun handleNotificationClick(context: Context, intent: Intent, notificationId: Int) {
        val appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, appLinks)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)

        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        if (intent.hasExtra(CMConstant.CouponCodeExtra.COUPON_CODE)) {
            val coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE)
            if (!TextUtils.isEmpty(coupon))
                copyToClipboard(context, coupon)
        }
    }

    private fun handleActionButtonClick(context: Context, intent: Intent, notificationId: Int) {
        if (intent.hasExtra(CMConstant.EXTRA_PRE_DEF_ACTION)) {
            val preDefineActions = intent.getParcelableExtra<PreDefineActions>(CMConstant.EXTRA_PRE_DEF_ACTION)
            handleShareActionButtonClick(context, preDefineActions)
        } else {
            if (!intent.hasExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK))
                return
            val appLinks = intent.getStringExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK)
            val appLinkIntent = RouteManager.getIntent(context.applicationContext, appLinks)
            appLinkIntent.putExtras(intent.extras!!)
            startActivity(context, appLinkIntent)
        }
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun handleCarouselButtonClick(context: Context, intent: Intent, notificationId: Int, isNext: Boolean) {
        try {
            val carouselList = intent.getParcelableArrayListExtra<Carousel>(CMConstant.ReceiverExtraData.CAROUSEL_DATA)
            var index = intent.getIntExtra(CMConstant.PayloadKeys.CAROUSEL_INDEX, 0)
            if (null == carouselList || carouselList.size == 0)
                return
            index = if (isNext) index + 1 else index - 1
            val bundle = intent.extras
            bundle!!.putString(CMConstant.PayloadKeys.NOTIFICATION_ID, notificationId.toString())
            bundle.putInt(CMConstant.PayloadKeys.CAROUSEL_INDEX, index)
            bundle.putString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
            val baseNotification = CMNotificationFactory.getNotification(context.applicationContext, bundle)
            postNotification(context, baseNotification!!)
        } catch (e: Exception) {
        }

    }

    private fun handleCarouselImageClick(context: Context, intent: Intent, notificationId: Int) {
        val (appLink) = intent.getParcelableExtra<Carousel>(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        launchCatchError(block = {
            CarouselUtilities.deleteCarouselImageDirectory(context)
        },
                onError = {
                    Timber.i(it)
                })
    }

    private fun postNotification(context: Context, baseNotification: BaseNotification) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = baseNotification.createNotification()
        notificationManager.notify(baseNotification.baseNotificationModel.notificationId, notification)
    }

    private fun startActivity(context: Context, intent: Intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.applicationContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun sendPushEvent(context: Context, eventName: String, campaignID: Int, notificationID: Int) {
        IrisAnalyticsEvents.sendPushEvent(context, eventName, campaignID.toString(), notificationID.toString())
    }

    private fun handleShareActionButtonClick(context: Context, preDefineActions: PreDefineActions) {
        try {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_SUBJECT, preDefineActions.title)
            share.putExtra(Intent.EXTRA_TEXT, preDefineActions.msg)
            val sharingIntent = Intent.createChooser(share, "Tokopedia")
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(sharingIntent)
        } catch (e: ActivityNotFoundException) {
        }

    }

    private fun applyPromoCode(context: Context, promoCode: String) {
        val promoCodeAutoApplyUseCase = PromoCodeAutoApplyUseCase(context)
        val requestParams = RequestParams.create()
        requestParams.putString(PromoCodeAutoApplyUseCase.PROMO_CODE, promoCode)
        promoCodeAutoApplyUseCase.createObservable(requestParams)
        promoCodeAutoApplyUseCase.execute(null)
    }

}
