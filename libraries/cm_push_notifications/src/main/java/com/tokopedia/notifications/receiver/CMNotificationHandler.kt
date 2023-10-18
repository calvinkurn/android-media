package com.tokopedia.notifications.receiver

import android.app.Activity
import android.app.Application
import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.commonpromo.PromoCodeAutoApplyUseCase
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.NetworkRouter
import com.tokopedia.notifications.R
import com.tokopedia.notifications.analytics.ProductAnalytics
import com.tokopedia.notifications.common.*
import com.tokopedia.notifications.data.DataManager
import com.tokopedia.notifications.di.DaggerCMNotificationComponent
import com.tokopedia.notifications.di.module.NotificationModule
import com.tokopedia.notifications.factory.CarouselNotification
import com.tokopedia.notifications.factory.ProductNotification
import com.tokopedia.notifications.factory.ReviewNotification
import com.tokopedia.notifications.model.*
import com.tokopedia.notifications.utils.NotificationCancelManager
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CMNotificationHandler : CoroutineScope {

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var userSession: UserSessionInterface

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private fun initGraphql(application: Application) {
        val authenticator = TkpdAuthenticatorGql(application, application as NetworkRouter, UserSession(application), RefreshTokenGql())
        GraphqlClient.init(application, authenticator)
    }

    private fun initInjector(context: Context) {
        try {
            val application = context.applicationContext as BaseMainApplication
            initGraphql(application)
            DaggerCMNotificationComponent.builder()
                .baseAppComponent(application.baseAppComponent)
                .notificationModule(NotificationModule(context))
                .build()
                .inject(this)
        } catch (e: Exception) {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT)
            messageMap["data"] = ""
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
        }
    }

    fun handleIntent(context: Context, intent: Intent) {
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
                        clearGroupNotificationFromTray(baseNotificationModel, context)
                        if (baseNotificationModel != null) {
                            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GENERAL, baseNotificationModel.elementId)
                        }
                    }

                    CMConstant.ReceiverAction.ACTION_VISUAL_COLLAPSED_CLICK -> {
                        handleVisualCollapsedClick(context, intent, notificationId, baseNotificationModel)
                    }

                    CMConstant.ReceiverAction.ACTION_VISUAL_EXPANDED_CLICK -> {
                        handleVisualExpandedClick(context, intent, notificationId, baseNotificationModel)
                    }

                    CMConstant.ReceiverAction.ACTION_BUTTON -> {
                        val actionButtonData = getActionButtonData(intent)
                        baseNotificationModel?.let {
                            handleActionButtonClick(
                                context,
                                intent,
                                notificationId,
                                it,
                                actionButtonData
                            )
                            if (includeEventSend(actionButtonData)) {
                                sendElementClickPushEvent(context, it, getActionElementId(actionButtonData))
                            }
                        }
                    }

                    CMConstant.ReceiverAction.ACTION_PERSISTENT_CLICK -> {
                        if (baseNotificationModel != null) {
                            handlePersistentClick(context, intent, baseNotificationModel)
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
                        handleGridMainClick(context, intent, notificationId, baseNotificationModel)
                    }

                    /*Image Carousel Handling*/
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_MAIN_CLICK -> {
                        handleCarouselMainClick(context, intent, notificationId, baseNotificationModel)
                    }
                    CMConstant.ReceiverAction.ACTION_CAROUSEL_IMAGE_CLICK -> {
                        // has Base Notification Model
                        baseNotificationModel?.let {
                            handleCarouselImageClick(context, intent, notificationId, baseNotificationModel)
                        }
                    }
                    CMConstant.ReceiverAction.ACTION_RIGHT_ARROW_CLICK -> {
                        CarouselNotification.onRightIconClick(context.applicationContext, baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_LEFT_ARROW_CLICK -> {
                        CarouselNotification.onLeftIconClick(context.applicationContext, baseNotificationModel!!)
                    }

                    CMConstant.ReceiverAction.ACTION_CAROUSEL_NOTIFICATION_DISMISS -> {
                        clearCarouselImages(context.applicationContext)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION)
                    }
                    /*Image Carousel Handling*/

                    /*Product Info Carousel Click Handling*/
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CLICK -> {
                        handleProductClick(context, intent, notificationId, baseNotificationModel)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_COLLAPSED_CLICK -> {
                        handleProductCollapsedClick(context, intent, notificationId, baseNotificationModel)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_LEFT_CLICK -> {
                        ProductNotification.onLeftIconClick(context.applicationContext, baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_CAROUSEL_RIGHT_CLICK -> {
                        ProductNotification.onRightIconClick(context.applicationContext, baseNotificationModel!!)
                    }
                    CMConstant.ReceiverAction.ACTION_PRODUCT_NOTIFICATION_DISMISS -> {
                        clearProductImages(context.applicationContext)
                        sendClickPushEvent(context, IrisAnalyticsEvents.PUSH_DISMISSED, baseNotificationModel, CMConstant.NotificationType.GENERAL)
                    }
                    CMConstant.ReceiverAction.ACTION_REVIEW_NOTIFICATION_STAR_CLICKED ->
                        handleReviewStarClick(context, notificationId, intent, baseNotificationModel)
                }
            }
        } catch (e: Exception) {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "exception"
            messageMap["err"] = Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT)
            messageMap["data"] = "$intent"
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap)
            e.printStackTrace()
        }
    }

    private fun clearGroupNotificationFromTray(
        baseNotificationModel: BaseNotificationModel?,
        context: Context
    ) {
        baseNotificationModel?.groupId?.let { id ->
            if (id.toString().isNotBlank() && id != 0) {
                NotificationCancelManager().clearNotificationsByGroup(context, id)
            }
        }
    }

    private fun handleReviewStarClick(
        context: Context,
        notificationId: Int,
        intent: Intent,
        baseNotificationModel: BaseNotificationModel?
    ) {
        val updatedBaseNotificationModel = ReviewNotification
            .updateReviewAppLink(intent, baseNotificationModel)
        handleNotificationClick(context, intent, notificationId, updatedBaseNotificationModel)
        baseNotificationModel?.let {
            sendElementClickPushEvent(
                context,
                IrisAnalyticsEvents.PUSH_CLICKED,
                baseNotificationModel,
                CMConstant.NotificationType.GENERAL,
                baseNotificationModel.elementId
            )
        }
    }

    private fun handleGridMainClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel?) {
        handleMainClick(context, intent, notificationId)
        baseNotificationModel?.let {
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.GRID_NOTIFICATION, baseNotificationModel.elementId)
        }
    }

    private fun handleVisualExpandedClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel?) {
        handleNotificationClick(context, intent, notificationId, baseNotificationModel)
        baseNotificationModel?.let {
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.VISUAL_NOTIIFICATION, baseNotificationModel.visualExpandedElementId)
        }
    }

    private fun handleVisualCollapsedClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel?) {
        handleNotificationClick(context, intent, notificationId, baseNotificationModel)
        baseNotificationModel?.let {
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.VISUAL_NOTIIFICATION, baseNotificationModel.visualCollapsedElementId)
        }
    }

    private fun handleMainClick(context: Context, intent: Intent, notificationId: Int) {
        val baseNotificationModel: BaseNotificationModel = intent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL) ?: BaseNotificationModel()
        startActivity(context, baseNotificationModel.appLink, intent)
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handleMainClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel) {
        startActivity(context, baseNotificationModel.appLink, intent)
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handleCarouselMainClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel?) {
        handleMainClick(context, intent, notificationId)
        baseNotificationModel?.let {
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, it, CMConstant.NotificationType.CAROUSEL_NOTIFICATION, it.elementId)
        }
        clearCarouselImages(context.applicationContext)
    }

    private fun handleProductClick(
        context: Context,
        intent: Intent,
        notificationId: Int,
        element: BaseNotificationModel?
    ) {
        val productInfo: ProductInfo = intent.getParcelableExtra(CMConstant.EXTRA_PRODUCT_INFO) ?: ProductInfo()

        element?.let {
            if (it.type == CMConstant.NotificationType.PRODUCT_NOTIIFICATION) {
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
        startActivity(context, productInfo.appLink, intent)
        NotificationManagerCompat.from(context).cancel(notificationId)
        clearProductImages(context.applicationContext)
        element?.let {
            sendElementClickPushEvent(
                context,
                IrisAnalyticsEvents.PUSH_CLICKED,
                it,
                CMConstant.NotificationType.PRODUCT_NOTIIFICATION,
                it.elementId
            )
        }
    }

    private fun handleProductCollapsedClick(
        context: Context,
        intent: Intent,
        notificationId: Int,
        baseNotificationModel: BaseNotificationModel?
    ) {
        val productInfo = baseNotificationModel?.productInfoList?.first()
        handleMainClick(context, intent, notificationId)
        ProductAnalytics.clickCollapsedBody(userSession.userId, baseNotificationModel, productInfo)
        clearProductImages(context.applicationContext)
        baseNotificationModel?.let {
            sendElementClickPushEvent(
                context,
                IrisAnalyticsEvents.PUSH_CLICKED,
                it,
                CMConstant.NotificationType.PRODUCT_NOTIIFICATION,
                it.elementId
            )
        }
    }

    private fun clearProductImages(context: Context) {
        launchCatchError(
            block = {
                CarouselUtilities.deleteProductImageDirectory(context.applicationContext)
            },
            onError = {
            }
        )
    }

    private fun clearCarouselImages(context: Context) {
        launchCatchError(
            block = {
                CarouselUtilities.deleteCarouselImageDirectory(context.applicationContext)
            },
            onError = {
            }
        )
    }

    private fun handleGridNotificationClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel) {
        val grid: Grid = intent.getParcelableExtra(CMConstant.ReceiverExtraData.EXTRA_GRID_DATA) ?: Grid()
        sendElementClickPushEvent(
            context,
            IrisAnalyticsEvents.PUSH_CLICKED,
            baseNotificationModel,
            CMConstant.NotificationType.GRID_NOTIFICATION,
            grid.element_id
        )
        startActivity(context, grid.appLink, intent)
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun cancelPersistentNotification(context: Context, notificationId: Int) {
        CMEvents.postGAEvent(
            PersistentEvent.EVENT,
            PersistentEvent.EVENT_CATEGORY,
            PersistentEvent.EVENT_ACTION_CANCELED,
            PersistentEvent.EVENT_LABEL
        )
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    private fun handlePersistentClick(context: Context, intent: Intent, baseNotificationModel: BaseNotificationModel) {
        if (intent.hasExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA)) {
            val persistentButton: PersistentButton = intent.getParcelableExtra(CMConstant.ReceiverExtraData.PERSISTENT_BUTTON_DATA) ?: PersistentButton()
            if (persistentButton.isAppLogo) {
                CMEvents.postGAEvent(
                    PersistentEvent.EVENT,
                    PersistentEvent.EVENT_CATEGORY,
                    PersistentEvent.EVENT_ACTION_LOGO_CLICK,
                    persistentButton.appLink ?: ""
                )
            } else {
                CMEvents.postGAEvent(
                    PersistentEvent.EVENT,
                    PersistentEvent.EVENT_CATEGORY,
                    persistentButton.text ?: "",
                    persistentButton.appLink ?: ""
                )
            }
            startActivity(context, persistentButton.appLink, intent)
            sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.PERSISTENT, persistentButton.element_id)
        }
    }

    private fun copyToClipboard(context: Context, contents: String) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Tokopedia", contents)
        clipboard.setPrimaryClip(clip)
        applyPromoCode(context, contents)
        Toast.makeText(context, context.getString(R.string.cm_tv_coupon_code_copied), Toast.LENGTH_LONG).show()
    }

    private fun handleNotificationClick(
        context: Context,
        intent: Intent,
        notificationId: Int,
        baseNotificationModel: BaseNotificationModel?
    ) {
        // Notification attribution
        dataManager.attribution(baseNotificationModel)
        baseNotificationModel?.let {
            handleMainClick(context, intent, notificationId, it)
        }
        handleCouponCode(intent, context)
    }

    private fun handleActionButtonClick(
        context: Context,
        intent: Intent,
        notificationId: Int,
        notificationData: BaseNotificationModel?,
        actionButtonData: ActionButton?
    ) {
        notificationData?.let { notifData ->
            actionButtonData?.apply {
                pdActions?.let {
                    if (it.type == CMConstant.PreDefineActionType.ATC || it.type == CMConstant.PreDefineActionType.OCC) {
                        handleProductPurchaseClick(context, notifData, this)
                    } else {
                        handleShareActionButtonClick(context, it, notifData)
                    }
                } ?: let {
                    it.type?.let { type ->
                        if (type == CMConstant.PayloadKeys.ADD_TO_CART) { // internal ATC
                            handleAddToCartProduct(notifData, it.addToCart)
                        } else if (type == CMConstant.PreDefineActionType.ATC || type == CMConstant.PreDefineActionType.OCC) { // external appLink
                            handleProductPurchaseClick(context, notifData, this)
                        } else { // applink handler for action button
                            startActivity(context, it.appLink, intent)
                        }
                    }
                }
            }
        }
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)
        handleCouponCode(intent, context)
    }

    private fun getActionButtonData(intent: Intent): ActionButton? {
        intent.getParcelableExtra<ActionButton?>(CMConstant.ReceiverExtraData.ACTION_BUTTON_EXTRA)
            ?.let {
                return it
            }
        return null
    }

    private fun getActionElementId(actionButton: ActionButton?): String? {
        actionButton?.let {
            return it.pdActions?.element_id ?: it.element_id
        }
        return null
    }

    private fun isPdActionButtonClick(actionButton: ActionButton?): Boolean {
        actionButton?.pdActions?.let {
            return it.type == CMConstant.PreDefineActionType.ATC ||
                it.type == CMConstant.PreDefineActionType.OCC
        }
        return false
    }

    private fun includeEventSend(actionButton: ActionButton?): Boolean {
        if (isPdActionButtonClick(actionButton)) {
            return true
        } else {
            actionButton?.pdActions?.let {
                return false
            }
            actionButton?.type?.let {
                return true
            }
            return false
        }
    }

    private fun handleCouponCode(intent: Intent, context: Context) {
        if (intent.hasExtra(CMConstant.CouponCodeExtra.COUPON_CODE)) {
            val coupon = intent.getStringExtra(CMConstant.CouponCodeExtra.COUPON_CODE)
            val gratificationId = intent.getStringExtra(CMConstant.CouponCodeExtra.GRATIFICATION_ID)
            if (!gratificationId.isNullOrEmpty()) {
                return
            }
            if (!coupon.isNullOrEmpty()) {
                copyToClipboard(context, coupon)
            }
        }
    }

    private fun handleProductPurchaseClick(
        context: Context,
        notificationData: BaseNotificationModel,
        actionButton: ActionButton
    ) {
        if (actionButton.type == CMConstant.PreDefineActionType.OCC) {
            ProductAnalytics.occCLickButton(
                userSession.userId,
                notificationData,
                notificationData.productInfoList
            )
        } else if (actionButton.type == CMConstant.PreDefineActionType.ATC) {
            ProductAnalytics.atcCLickButton(notificationData, notificationData.productInfoList)
        }

        startActivity(context, actionButton.appLink, null)
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
            val userId = it.userId ?: ""
            dataManager.atcProduct(templateKey, userId, addToCart)
        }
    }

    private fun handleCarouselImageClick(context: Context, intent: Intent, notificationId: Int, baseNotificationModel: BaseNotificationModel) {
        val (appLink) = intent.getParcelableExtra(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM) ?: Carousel()
        val carousel = intent.getParcelableExtra<Carousel>(CMConstant.ReceiverExtraData.CAROUSEL_DATA_ITEM)
        startActivity(context, appLink, intent)
        NotificationManagerCompat.from(context.applicationContext).cancel(notificationId)

        sendElementClickPushEvent(context, IrisAnalyticsEvents.PUSH_CLICKED, baseNotificationModel, CMConstant.NotificationType.CAROUSEL_NOTIFICATION, carousel?.element_id ?: "")

        launchCatchError(
            block = {
                CarouselUtilities.deleteCarouselImageDirectory(context)
            },
            onError = {
            }
        )
    }

    private fun startActivity(
        context: Context,
        appLink: String?,
        dataIntent: Intent?
    ) {
        try {
            val appLinkIntent = getAppLinkIntent(context, appLink)
            copyDataIntentToAppLinkIntent(appLinkIntent, dataIntent)
            appLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.applicationContext.startActivity(appLinkIntent)
            CMNotificationUtils.sendUTMParamsInGTM(appLink)
        } catch (e: Exception) {
        }
    }

    private fun copyDataIntentToAppLinkIntent(
        appLinkIntent: Intent,
        dataIntent: Intent?
    ) {
        try {
            dataIntent?.let { dataIntent ->
                // this extra data is added to support gratification
                if (dataIntent.hasExtra(CMConstant.CouponCodeExtra.GRATIFICATION_ID)) {
                    appLinkIntent.putExtra(CMConstant.EXTRA_BASE_MODEL, true)
                    appLinkIntent.putExtra(
                        CMConstant.CouponCodeExtra.GRATIFICATION_ID,
                        dataIntent.getStringExtra(CMConstant.CouponCodeExtra.GRATIFICATION_ID)
                    )
                }
                // to support video push and extra params
                appLinkIntent.putExtras(getCustomDataBundle(dataIntent))
            }
        } catch (e: Exception) {}
    }

    private fun getCustomDataBundle(dataIntent: Intent): Bundle {
        val baseNotificationModel: BaseNotificationModel? =
            dataIntent.getParcelableExtra(CMConstant.EXTRA_BASE_MODEL)
        var bundle = Bundle()
        if (baseNotificationModel != null) {
            baseNotificationModel.videoPushModel?.let {
                if (it.isNotBlank()) {
                    bundle = jsonToBundle(bundle, JSONObject(it))
                }
            }
            baseNotificationModel.customValues?.let {
                if (it.isNotEmpty()) {
                    bundle = jsonToBundle(bundle, JSONObject(it))
                }
            }
        }
        bundle.putString(
            CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY,
            baseNotificationModel?.webHookData()?.notificationTemplateKey.toString()
        )
        return bundle
    }

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

    private fun getAppLinkIntent(context: Context, appLink: String?): Intent {
        return RouteManager.getIntent(context.applicationContext, appLink ?: ApplinkConst.HOME)
    }

    private fun sendClickPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel?, pushType: String) {
        baseNotificationModel?.let {
            IrisAnalyticsEvents.sendPushEvent(context, eventName, baseNotificationModel)
        }
    }

    private fun sendElementClickPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, pushType: String, elementId: String?) {
        baseNotificationModel.let {
            IrisAnalyticsEvents.sendPushEvent(context, eventName, baseNotificationModel, elementId)
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

    companion object {
        @JvmStatic
        val instance: CMNotificationHandler = CMNotificationHandler()
    }
}
