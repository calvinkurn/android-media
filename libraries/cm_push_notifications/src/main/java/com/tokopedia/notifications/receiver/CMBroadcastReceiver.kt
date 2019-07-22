package com.tokopedia.notifications.receiver

import android.app.Activity
import android.content.*
import android.support.v4.app.NotificationManagerCompat
import android.text.TextUtils
import android.widget.Toast
import com.tokopedia.applink.RouteManager
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.factory.CarouselNotification
import com.tokopedia.notifications.factory.ProductNotification
import com.tokopedia.notifications.model.*
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            val baseNotificationModel: BaseNotificationModel? = intent.getParcelableExtra<BaseNotificationModel>(CMConstant.EXTRA_BASE_MODEL)
            if (null != action) {
                when (action) {

                    CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS -> {
                        NotificationManagerCompat.from(context).cancel(notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK -> {
                        handleNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,  baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_BUTTON -> {
                        handleActionButtonClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,  baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK -> {
                        handlePersistentClick(context, intent)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,  baseNotificationModel, CMConstant.NotificationType.PERSISTENT)
                    }
                    CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT -> {
                        cancelPersistentNotification(context, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,  baseNotificationModel, CMConstant.NotificationType.PERSISTENT)
                    }


                    CMConstant.ReceiverAction.ACTION_GRID_CLICK -> {
                        handleGridNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel,CMConstant.NotificationType.GRID_NOTIFICATION)
                    }

                    CMConstant.ReceiverAction.ACTION_GRID_MAIN_CLICK -> {
                        handleMainClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel,CMConstant.NotificationType.GRID_NOTIFICATION)

                    }

                    /*Image Carousel Handling*/
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_MAIN_CLICK -> {
                        //has Base Notification Model
                        handleCarouselMainClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,baseNotificationModel ,CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
                    }
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK -> {
                        //has Base Notification Model
                        handleCarouselImageClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
                    }
                    CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK -> {
                        //has Base Notification Model
                        CarouselNotification.onRightIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }
                    CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK -> {
                        //has Base Notification Model
                        CarouselNotification.onLeftIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }

                    CMConstant.ReceiverAction.ACTION_CAROUSEL_NOTIFICATION_DISMISS -> {
                        //has Base Notification Model
                        clearCarouselImages(context.applicationContext)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED,baseNotificationModel ,CMConstant.NotificationType.CAROUSEL_NOTIFICATION)

                    }
                    /*Image Carousel Handling*/

                    /*Product Info Carousel Click Handling*/
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK -> {
                        //has Base Notification Model
                        handleProductClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel , CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK -> {
                        //has Base Notification Model
                        handleCollapsedViewClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel , CMConstant.NotificationType.GENERAL)

                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_LEFT_CLICK -> {
                        //has Base Notification Model
                        ProductNotification.onLeftIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK -> {
                        //has Base Notification Model
                        ProductNotification.onRightIconClick(context.applicationContext,
                                intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL))
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS -> {
                        //has Base Notification Model
                        clearProductImages(context.applicationContext)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED,baseNotificationModel ,CMConstant.NotificationType.GENERAL)

                    }
                    /*Product Info Carousel Click Handling*/

                }
            }
        } catch (e: Exception) {
        }
    }

    private fun handleMainClick(context: Context, intent: Intent, notificationId: Int) {
        val baseNotificationModel: BaseNotificationModel = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, baseNotificationModel.appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handleCarouselMainClick(context: Context, intent: Intent, notificationId: Int) {
        handleMainClick(context, intent, notificationId)
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
        handleMainClick(context, intent, notificationId)
        clearProductImages(context.applicationContext)
    }

    private fun clearProductImages(context: Context) {
        launchCatchError(block = {
            CarouselUtilities.deleteProductImageDirectory(context.applicationContext)
        },
                onError = {
                })
    }

    private fun clearCarouselImages(context: Context) {
        launchCatchError(block = {
            CarouselUtilities.deleteCarouselImageDirectory(context.applicationContext)
        },
                onError = {
                })
    }

    private fun handleGridNotificationClick(context: Context, intent: Intent, notificationId: Int) {
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        val grid: Grid = intent.getParcelableExtra(CMConstant.ReceiverExtraData.EXTRA_GRID_DATA)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, grid.appLink)
        appLinkIntent.putExtras(intent.extras!!)
        startActivity(context, appLinkIntent)
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
            val persistentButton: PersistentButton = intent.getParcelableExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)
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
        handleMainClick(context, intent, notificationId)
        if (intent.hasExtra(CMConstant.CouponCodeExtra.COUPON_CODE)) {
            val coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE)
            if (!TextUtils.isEmpty(coupon))
                copyToClipboard(context, coupon)
        }
    }

    private fun handleActionButtonClick(context: Context, intent: Intent, notificationId: Int) {
        val actionButton: ActionButton? = intent.getParcelableExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA)
        actionButton?.apply {
            pdActions?.let {
                handleShareActionButtonClick(context, it)
            } ?: let {
                val appLinkIntent = RouteManager.getIntent(context.applicationContext, it.appLink)
                appLinkIntent.putExtras(intent.extras!!)
                startActivity(context, appLinkIntent)
            }
        }
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
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
                    //Timber.i(it)
                })
    }

    private fun startActivity(context: Context, intent: Intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.applicationContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel?, pushType: String) {
        baseNotificationModel?.let {
            IrisAnalyticsEvents.sendPushEvent(context, eventName, baseNotificationModel, pushType)
        }
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


/* override fun onReceive(context: Context, intent: Intent) {
        try {
            val action = intent.action
            if (!intent.hasExtra(CMConstant.EXTRA_NOTIFICATION_ID))
                return
            val notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0)
            val campaignId = intent.getIntExtra(CMConstant.EXTRA_CAMPAIGN_ID, 0)
            val parentId = intent.getIntExtra(CMConstant.EXTRA_PARENT_ID, 0)
            if (null != action) {

                when (action) {
                    CMConstant.ReceiverAction.ACTION_BUTTON -> {
                        handleActionButtonClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId, CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK -> {
                        handlePersistentClick(context, intent)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId, CMConstant.NotificationType.PERSISTENT)
                    }
                    CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT -> {
                        cancelPersistentNotification(context, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId, CMConstant.NotificationType.PERSISTENT)
                    }
                    CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS -> {
                        NotificationManagerCompat.from(context).cancel(notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, campaignId, notificationId, parentId, CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK -> {
                        handleNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId,CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK -> handleCarouselButtonClick(context, intent, notificationId, true)
                    CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK -> handleCarouselButtonClick(context, intent, notificationId, false)
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK -> {
                        handleCarouselImageClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
                    }
                    CMConstant.ReceiverAction.ACTION_GRID_CLICK -> {
                        handleGridNotificationClick(context, intent, notificationId)
                        sendPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, campaignId, notificationId, parentId, CMConstant.NotificationType.GRID_NOTIFICATION)
                    }
                }
            }
        } catch (e: Exception) {
        }
    }*/
