package com.tokopedia.notifications.receiver

import android.app.Activity
import android.content.*
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.ProductAnalytics
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.common.CMConstant.NotificationType.PRODUCT_NOTIIFICATION
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.ADD_TO_CART
import com.tokopedia.notifications.common.CMConstant.PreDefineActionType.ATC
import com.tokopedia.notifications.common.CMConstant.PreDefineActionType.OCC
import com.tokopedia.notifications.common.CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA
import com.tokopedia.notifications.data.DataManager
import com.tokopedia.notifications.di.DaggerCMNotificationComponent
import com.tokopedia.notifications.di.module.NotificationModule
import com.tokopedia.notifications.factory.CarouselNotification
import com.tokopedia.notifications.factory.ProductNotification
import com.tokopedia.notifications.model.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * @author lalit.singh
 */
class CMBroadcastReceiver : BroadcastReceiver(), CoroutineScope {

    @Inject lateinit var dataManager: DataManager
    @Inject lateinit var userSession: UserSessionInterface

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private fun initInjector(context: Context) {
        DaggerCMNotificationComponent.builder()
                .notificationModule(NotificationModule(context))
                .build()
                .inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        initInjector(context)

        try {
            val action = intent.action
            if (!intent.hasExtra(CMConstant.EXTRA_NOTIFICATION_ID)) return
            val notificationId = intent.getIntExtra(CMConstant.EXTRA_NOTIFICATION_ID, 0)
            val baseNotificationModel: BaseNotificationModel? = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
            if (action != null) {
                when (action) {
                    CMConstant.ReceiverAction.ACTION_ON_NOTIFICATION_DISMISS -> {
                        NotificationManagerCompat.from(context).cancel(notificationId)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_BANNER_CLICK -> {
                        handleNotificationClick(context, intent, notificationId, baseNotificationModel)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_NOTIFICATION_CLICK -> {
                        handleNotificationClick(context, intent, notificationId, baseNotificationModel)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }

                    CMConstant.ReceiverAction.ACTION_BUTTON -> {
                        if (baseNotificationModel != null) {
                            handleActionButtonClick(context, intent, notificationId, baseNotificationModel)
                        }
                    }

                    CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK -> {
                        if (baseNotificationModel != null) {
                            handlePersistentClick(context, intent,baseNotificationModel)
                        }
                    }
                    CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT -> {
                        cancelPersistentNotification(context, notificationId)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.PERSISTENT)
                    }


                    CMConstant.ReceiverAction.ACTION_GRID_CLICK -> {
                        baseNotificationModel?.let {
                            handleGridNotificationClick(context, intent, notificationId, baseNotificationModel)
                        }
                    }

                    CMConstant.ReceiverAction.ACTION_GRID_MAIN_CLICK -> {
                        handleMainClick(context, intent, notificationId)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GRID_NOTIFICATION)

                    }

                    /*Image Carousel Handling*/
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_MAIN_CLICK -> {
                        handleCarouselMainClick(context, intent, notificationId)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
                    }
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK -> {
                        //has Base Notification Model
                        baseNotificationModel?.let {
                            handleCarouselImageClick(context, intent, notificationId, baseNotificationModel)
                        }

                    }
                    CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK -> {
                        CarouselNotification.onRightIconClick(context.applicationContext,baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK -> {
                        CarouselNotification.onLeftIconClick(context.applicationContext,baseNotificationModel!!)
                    }

                    CMConstant.ReceiverAction.ACTION_CAROUSEL_NOTIFICATION_DISMISS -> {
                        clearCarouselImages(context.applicationContext)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)

                    }
                    /*Image Carousel Handling*/

                    /*Product Info Carousel Click Handling*/
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK -> {
                        handleProductClick(context, intent, notificationId, baseNotificationModel)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK -> {
                        handleCollapsedViewClick(context, intent, notificationId)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_LEFT_CLICK -> {
                        ProductNotification.onLeftIconClick(context.applicationContext,baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK -> {
                        ProductNotification.onRightIconClick(context.applicationContext,baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS -> {
                        clearProductImages(context.applicationContext)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleMainClick(context: Context, intent: Intent, notificationId: Int) {
        val baseNotificationModel: BaseNotificationModel = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, baseNotificationModel.appLink?:ApplinkConst.HOME)
        intent.extras?.let { bundle ->
            appLinkIntent.putExtras(bundle)
        }
        startActivity(context, appLinkIntent)
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handleCarouselMainClick(context: Context, intent: Intent, notificationId: Int) {
        handleMainClick(context, intent, notificationId)
        clearCarouselImages(context.applicationContext)
    }

    private fun handleProductClick(
            context: Context,
            intent: Intent,
            notificationId: Int,
            element: BaseNotificationModel?
    ) {
        val productInfo: ProductInfo = intent.getParcelableExtra(CMConstant.EXTRA_PRODUCT_INFO)

        element?.let {
            if (it.type == PRODUCT_NOTIIFICATION) {
                // tracker for general body click
                ProductAnalytics.clickBody(
                        userSession.userId,
                        element,
                        productInfo
                )

                // tracker for product card click
                ProductAnalytics.clickProductCard(userSession.userId, it, productInfo)
            }
        }

        val appLinkIntent = RouteManager.getIntent(
                context.applicationContext,
                productInfo.appLink?: ApplinkConst.HOME
        )

        intent.extras?.let { appLinkIntent.putExtras(it) }
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

    private fun handleGridNotificationClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel) {
        context.applicationContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        val grid: Grid = intent.getParcelableExtra(CMConstant.ReceiverExtraData.EXTRA_GRID_DATA)
        sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED,
                baseNotificationModel, CMConstant.NotificationType.GRID_NOTIFICATION, grid.element_id)
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, grid.appLink?:ApplinkConst.HOME)
        intent.extras?.let {
            appLinkIntent.putExtras(it)
        }
        startActivity(context, appLinkIntent)
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun cancelPersistentNotification(context: Context, notificationId: Int) {
        CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                PersistentEvent.EVENT_ACTION_CANCELED, PersistentEvent.EVENT_LABEL)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handlePersistentClick(context: Context, intent: Intent, baseNotificationModel: BaseNotificationModel) {
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        if (intent.hasExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)) {
            val persistentButton: PersistentButton = intent.getParcelableExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)
            if (persistentButton.isAppLogo) {
                CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                        PersistentEvent.EVENT_ACTION_LOGO_CLICK, persistentButton.appLink?:"")
            } else {
                CMEvents.postGAEvent(PersistentEvent.EVENT, PersistentEvent.EVENT_CATEGORY,
                        persistentButton.text?:"", persistentButton.appLink?:"")
            }
            val appLinkIntent = RouteManager.getIntent(context.applicationContext, persistentButton.appLink?:ApplinkConst.HOME)
            intent.extras?.let {
                appLinkIntent.putExtras(it)
            }
            startActivity(context, appLinkIntent)
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.PERSISTENT, persistentButton.element_id)

        }
    }

    private fun copyToClipboard(context: Context, contents: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", contents)
        clipboard.setPrimaryClip(clip)
        applyPromoCode(context, contents)
        Toast.makeText(context, "${context.getString(R.string.cm_tv_coupon_code_copied)} $contents"
                , Toast.LENGTH_LONG).show()
    }

    private fun handleNotificationClick(
            context: Context,
            intent: Intent,
            notificationId: Int,
            baseNotificationModel: BaseNotificationModel?
    ) {
        // Notification attribution
        dataManager.attribution(baseNotificationModel)

        handleMainClick(context, intent, notificationId)
        if (intent.hasExtra(CMConstant.CouponCodeExtra.COUPON_CODE)) {
            val coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE)
            if (!TextUtils.isEmpty(coupon))
                copyToClipboard(context, coupon)
        }
    }

    private fun handleActionButtonClick(
            context: Context,
            intent: Intent,
            notificationId: Int,
            notificationData: BaseNotificationModel
    ) {
        intent.getParcelableExtra<ActionButton?>(ACTION_BUTTON_EXTRA)?.apply {
            pdActions?.let {
                if (it.type == ATC || it.type == OCC) {
                    handleProductPurchaseClick(context, notificationData, this)
                } else {
                    handleShareActionButtonClick(context, it, notificationData)
                }
            }?: let {
                it.type?.let { type ->
                    if (type == ADD_TO_CART) { // internal ATC
                        handleAddToCartProduct(notificationData, it.addToCart)
                    } else if (it.type == ATC || it.type == OCC) { // external appLink
                        handleProductPurchaseClick(context, notificationData, this)
                    }
                }

                // applink handler for action button
                val appLinkIntent = RouteManager.getIntent(
                        context.applicationContext,
                        it.appLink?: ApplinkConst.HOME
                )
                intent.extras?.let { bundle ->
                    appLinkIntent.putExtras(bundle)
                }
                startActivity(context, appLinkIntent)
                sendElementClickPushEvent(
                        context,
                        notificationData,
                        it.element_id
                )
            }
        }
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun handleProductPurchaseClick(
            context: Context,
            notificationData: BaseNotificationModel,
            actionButton: ActionButton
    ) {
        if (actionButton.type == OCC) {
            ProductAnalytics.occCLickButton(
                    userSession.userId,
                    notificationData,
                    notificationData.productInfoList
            )
        } else if (actionButton.type == ATC) {
            ProductAnalytics.atcCLickButton(notificationData, notificationData.productInfoList)
        }

        actionButton.let {
            val intent = RouteManager.getIntent(context.applicationContext, it.appLink)
            startActivity(context, intent)

            sendElementClickPushEvent(
                    context,
                    notificationData,
                    it.pdActions?.element_id
            )
        }
    }

    private fun sendElementClickPushEvent(
            context: Context,
            notificationData: BaseNotificationModel,
            elementId: String?
    ) {
        sendElementClickPushEvent(
                context,
                IrisAnalyticsEvents.PUSH_CLICKED,
                notificationData,
                CMConstant.NotificationType.GENERAL,
                elementId
        )
    }

    private fun handleAddToCartProduct(data: BaseNotificationModel?, addToCart: AddToCart?) {
        data?.let {
            val templateKey = it.campaignId.toString()
            val userId = it.userId?: ""
            dataManager.atcProduct(templateKey, userId, addToCart)
        }
    }

    private fun handleCarouselImageClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel) {
        var (appLink) = intent.getParcelableExtra<Carousel>(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM)
        val carousel = intent.getParcelableExtra<Carousel>(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM);
        val appLinkIntent = RouteManager.getIntent(context.applicationContext, appLink?:ApplinkConst.HOME)
        intent.extras?.let { bundle->
            appLinkIntent.putExtras(bundle)
        }
        startActivity(context, appLinkIntent)
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION, carousel.element_id)

        launchCatchError(block = {
            CarouselUtilities.deleteCarouselImageDirectory(context)
        },
                onError = {
                })
    }

    private fun startActivity(context: Context, intent: Intent) {
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.applicationContext.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun sendClickPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel?, pushType: String) {
        baseNotificationModel?.let {
            IrisAnalyticsEvents.sendPushEvent(context, eventName, baseNotificationModel)
        }
    }

    private fun sendElementClickPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, pushType: String, elementId: String?) {
        baseNotificationModel?.let {
            IrisAnalyticsEvents.sendPushEvent(context, eventName, baseNotificationModel,elementId)
        }
    }

    private fun handleShareActionButtonClick(context: Context, preDefineActions: PreDefineActions, baseNotificationModel: BaseNotificationModel) {
        try {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_SUBJECT, preDefineActions.title)
            share.putExtra(Intent.EXTRA_TEXT, preDefineActions.msg)
            val sharingIntent = Intent.createChooser(share, "Tokopedia")
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(sharingIntent)
            sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
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