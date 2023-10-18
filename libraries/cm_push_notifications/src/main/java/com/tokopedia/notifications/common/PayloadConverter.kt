package com.tokopedia.notifications.common

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.notification.common.utils.NotificationTargetPriorities
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.*
import com.tokopedia.notifications.model.*
import com.tokopedia.notifications.model.payload_extra.Topchat
import org.json.JSONObject
import kotlin.collections.ArrayList
import com.tokopedia.notification.common.utils.NotificationValidationManager.NotificationPriorityType as NotificationPriorityType

const val HOURS_24_IN_MILLIS: Long = 24 * 60 * 60 * 1000L

object PayloadConverter {

    private val TAG = PayloadConverter::class.java.simpleName

    fun convertMapToBundle(map: Map<String, String>?): Bundle {
        val bundle = Bundle(map?.size ?: 0)
        if (map != null) {
            for ((key, value) in map) {
                bundle.putString(key, value)
            }
        }
        return bundle
    }

    fun convertToBaseModel(data: Bundle): BaseNotificationModel {
        val model = BaseNotificationModel()
        model.icon = data.getString(ICON, "")
        if (data.containsKey(NOTIFICATION_PRIORITY)) {
            model.priorityPreOreo = data.getString(NOTIFICATION_PRIORITY, "2").toIntOrZero()
        }
        model.notificationId = data.getString(NOTIFICATION_ID, "500").toIntOrZero()
        model.campaignId = data.getString(CAMPAIGN_ID, "0").toLongOrZero()
        model.parentId = data.getString(PARENT_ID, "0").toLongOrZero()
        model.elementId = data.getString(ELEMENT_ID, "")
        model.tribeKey = data.getString(TRIBE_KEY, "")
        model.type = data.getString(NOTIFICATION_TYPE, "")
        model.pushPayloadExtra = PushPayloadExtra(
            isReviewNotif = isBooleanTrue(data, IS_REVIEW),
            replyType = data.getString(REPLY_TYPE, ""),
            userType = data.getString(USER_TYPE, "")
        )

        setNotificationSound(model = model, extras = data)

        model.title = data.getString(TITLE, "")
        model.detailMessage = data.getString(DESCRIPTION, "")
        model.message = data.getString(MESSAGE, "")
        model.media = getMedia(data)
        model.appLink = data.getString(APP_LINK, ApplinkConst.HOME)
        val actionButtonList = getActionButtons(data)
        if (actionButtonList != null) {
            model.actionButton = actionButtonList
        }
        model.persistentButtonList = getPersistentNotificationData(data)
        model.videoPushModel = data.getString(VIDEO_DATA, "")
        model.customValues = data.getString(CUSTOM_VALUE, "")
        val carouselList = getCarouselList(data)
        if (carouselList != null) {
            model.carouselList = carouselList
            model.carouselIndex = data.getInt(CAROUSEL_INDEX, 0)
        }
        model.isVibration = isBooleanTrue(data, VIBRATE)
        model.isUpdateExisting = isBooleanTrue(data, UPDATE_NOTIFICATION)
        model.isTest = isBooleanTrue(data, IS_TEST)
        val gridList = getGridList(data)
        if (gridList != null) {
            model.gridList = gridList
        }
        val productInfoList = getProductInfoList(data)
        if (productInfoList != null) {
            model.productInfoList = productInfoList
        }
        model.subText = data.getString(SUB_TEXT)
        model.visualCollapsedImageUrl = data.getString(VISUAL_COLLAPSED_IMAGE)
        model.visualCollapsedElementId = data.getString(VISUAL_COLLAPSED_ELEMENT_ID)
        model.visualExpandedImageUrl = data.getString(VISUAL_EXPANDED_IMAGE)
        model.visualExpandedElementId = data.getString(VISUAL_EXPANDED_ELEMENT_ID)
        model.campaignUserToken = data.getString(CAMPAIGN_USER_TOKEN, "")

        model.notificationMode = getNotificationMode(data)

        // start end time,
        model.startTime = dataToLong(data, NOTIFICATION_START_TIME)
        model.endTime = dataToLong(data, NOTIFICATION_END_TIME)

        if (model.notificationMode != NotificationMode.OFFLINE && (
            model.startTime == 0L ||
                model.endTime == 0L
            )
        ) {
            model.startTime = System.currentTimeMillis()
            model.endTime = System.currentTimeMillis() + CMPushNotificationManager.instance.cmPushEndTimeInterval
        }

        model.status = NotificationStatus.PENDING
        model.notificationProductType = data.getString(NOTIFICATION_PRODUCT_TYPE)

        // notification attribution
        model.transactionId = data.getString(TRANSACTION_ID)
        model.userTransactionId = data.getString(USER_TRANSACTION_ID)
        model.userId = data.getString(USER_ID)
        model.shopId = data.getString(SHOP_ID)
        model.isBigImage = isBooleanTrue(data, IS_BIG_IMAGE)
        model.blastId = data.getString(BLAST_ID)

        // webHook parameters
        model.webHookParam = data.getString(WEBHOOK_PARAM)

        model.payloadExtra = getPayloadExtras(data)
        model.groupId = data.getString(GROUP_ID, "0").toIntOrZero()
        model.groupName = data.getString(GROUP_NAME)

        return model
    }

    fun convertToBaseModel(data: SerializedNotificationData): BaseNotificationModel {
        val model = BaseNotificationModel()
        model.icon = data.icon
        model.priorityPreOreo = data.priorityPreOreo ?: 2
        model.notificationId = data.notificationId ?: 500
        model.campaignId = data.campaignId ?: 0
        model.parentId = data.parentId ?: 0
        model.elementId = data.elementId
        model.tribeKey = data.tribeKey
        model.type = data.type
        model.pushPayloadExtra = PushPayloadExtra(
            isReviewNotif = data.isReviewNotif,
            replyType = data.replyType ?: "",
            userType = data.userType ?: ""
        )

        setNotificationSound(model, data)

        model.title = data.title
        model.detailMessage = data.detailMessage
        model.message = data.message
        model.media = data.media
        model.appLink = data.appLink ?: ApplinkConst.HOME
        data.actionButton?.let {
            val actionButtonList = ArrayList<ActionButton>()
            it.forEach { actionButtonNullable ->
                actionButtonNullable?.let { actionButtonNonNullable ->
                    actionButtonList.add(actionButtonNonNullable)
                }
            }
            model.actionButton = actionButtonList
        }
        data.persistentButtonList?.let {
            val persButtonList = ArrayList<PersistentButton>()
            it.forEach { persButtonNullable ->
                persButtonNullable?.let { persButtonNonNullable ->
                    persButtonList.add(persButtonNonNullable)
                }
            }
            model.persistentButtonList = persButtonList
        }
        model.videoPushModel = data.videoPushModel ?: ""
        model.customValues = data.customValues?.run {
            this.toString()
        }
        data.carouselList?.let {
            val carouselList = ArrayList<Carousel>()
            it.forEach { carouselNullable ->
                carouselNullable?.let { carouselNonNullable ->
                    carouselList.add(carouselNonNullable)
                }
            }
            model.carouselList = carouselList
            model.carouselIndex = data.carouselIndex ?: 0
        }

        model.isVibration = data.isVibration ?: true
        model.isUpdateExisting = data.isUpdateExisting ?: false
        model.isTest = data.isTest == true

        data.gridList?.let {
            val gridList = ArrayList<Grid>()
            it.forEach { gridNullable ->
                gridNullable?.let { gridNonNullable ->
                    gridList.add(gridNonNullable)
                }
            }
            model.gridList = gridList
        }

        data.productInfoList?.let {
            val productList = ArrayList<ProductInfo>()
            it.forEach { productNullable ->
                productNullable?.let { productNonNullable ->
                    productList.add(productNonNullable)
                }
            }
            model.productInfoList = productList
        }
        model.subText = data.subText
        model.visualCollapsedImageUrl = data.visualCollapsedImageUrl
        model.visualExpandedImageUrl = data.visualExpandedImageUrl
        model.visualCollapsedElementId = data.visualCollapsedElementId
        model.visualExpandedElementId = data.visualExpandedElementId
        model.campaignUserToken = data.campaignUserToken

        model.notificationMode = if (data.notificationMode == true) NotificationMode.OFFLINE else NotificationMode.POST_NOW

        // start end time,
        model.startTime = data.startTime.toLongOrZero()
        model.endTime = data.endTime.toLongOrZero()

        if (model.notificationMode != NotificationMode.OFFLINE && (
            model.startTime == 0L ||
                model.endTime == 0L
            )
        ) {
            model.startTime = System.currentTimeMillis()
            model.endTime = System.currentTimeMillis() + CMPushNotificationManager.instance.cmPushEndTimeInterval
        }

        model.status = NotificationStatus.PENDING
        model.notificationProductType = data.notificationProductType

        // notification attribution
        model.transactionId = data.transactionId
        model.userTransactionId = data.userTransactionId
        model.userId = data.userId
        model.shopId = data.shopId
        model.isBigImage = data.isBigImage ?: false
        model.blastId = data.blastId

        // webHook parameters
        model.webHookParam = data.webHookParam

        model.payloadExtra = getPayloadExtras(data)
        model.groupId = data.groupId ?: 0
        model.groupName = data.groupName
        return model
    }

    fun advanceTargetNotification(bundle: Bundle): NotificationTargetPriorities {
        val mainAppPriority = bundle.getString(MAIN_APP_PRIORITY, "1")
        val sellerAppPriority = bundle.getString(SELLER_APP_PRIORITY, "1")
        val isAdvanceTarget = isBooleanTrue(bundle, ADVANCE_TARGET)

        val appPriorities = when {
            mainAppPriority.toIntOrZero() < sellerAppPriority.toIntOrZero() -> NotificationPriorityType.MainApp
            mainAppPriority.toIntOrZero() > sellerAppPriority.toIntOrZero() -> NotificationPriorityType.SellerApp
            else -> NotificationPriorityType.Both
        }

        return NotificationTargetPriorities(appPriorities, isAdvanceTarget)
    }

    public fun isBooleanTrue(data: Bundle, key: String): Boolean {
        return try {
            return data.containsKey(key) && data.getString(key)?.toBoolean() == true
        } catch (e: Exception) {
            false
        }
    }

    private fun dataToLong(data: Bundle, key: String): Long {
        return try {
            return data.getString(key)?.toLong() ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun getNotificationMode(data: Bundle): NotificationMode {
        return if (isBooleanTrue(data, NOTIFICATION_MODE)) {
            NotificationMode.OFFLINE
        } else {
            NotificationMode.POST_NOW
        }
    }

    private fun setNotificationSound(
        model: BaseNotificationModel,
        extras: Bundle
    ) {
        model.soundFileName = extras.getString(NOTIFICATION_SOUND, "")
        model.channelName = extras.getString(NOTIFICATION_CHANNEL, "")
    }

    private fun setNotificationSound(
        model: BaseNotificationModel,
        data: SerializedNotificationData
    ) {
        model.soundFileName = data.notificationSound ?: ""
        model.channelName = data.notificationChannel ?: ""
    }

    private fun getMedia(extras: Bundle): Media? {
        val actions = extras.getString(MEDIA)
        if (TextUtils.isEmpty(actions)) {
            return null
        }
        try {
            return Gson().fromJson(actions, Media::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getMedia", e)
        }

        return null
    }

    private fun getActionButtons(extras: Bundle): ArrayList<ActionButton>? {
        val actions = extras.getString(ACTION_BUTTON)
        if (TextUtils.isEmpty(actions)) {
            return null
        }
        try {
            val gson = Gson()
            val actionButtonListType = object : TypeToken<ArrayList<ActionButton>>() {
            }.type
            return gson.fromJson<ArrayList<ActionButton>>(actions, actionButtonListType)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getActionButtons", e)
        }

        return null
    }

    private fun getPersistentNotificationData(bundle: Bundle): ArrayList<PersistentButton>? {
        val persistentData = bundle.getString(PERSISTENT_DATA)
        if (TextUtils.isEmpty(persistentData)) {
            return null
        }
        try {
            val listType = object : TypeToken<ArrayList<PersistentButton>>() {
            }.type
            return Gson().fromJson<ArrayList<PersistentButton>>(persistentData, listType)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getPersistentNotificationData", e)
        }

        return null
    }

    private fun getProductInfoList(bundle: Bundle): ArrayList<ProductInfo>? {
        val productInfoListStr = bundle.getString(PRODUCT_INFO_LIST)
        if (TextUtils.isEmpty(productInfoListStr)) {
            return null
        }
        try {
            val listType = object : TypeToken<ArrayList<ProductInfo>>() {
            }.type
            return Gson().fromJson<ArrayList<ProductInfo>>(productInfoListStr, listType)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getProductInfo", e)
        }

        return null
    }

    private fun getGridList(bundle: Bundle): ArrayList<Grid>? {
        val persistentData = bundle.getString(GRID_DATA)
        if (TextUtils.isEmpty(persistentData)) {
            return null
        }
        try {
            val listType = object : TypeToken<ArrayList<Grid>>() {
            }.type
            return Gson().fromJson<ArrayList<Grid>>(persistentData, listType)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getGridList", e)
        }

        return null
    }

    private fun getVideoNotificationData(bundle: Bundle): JSONObject? {
        val values = bundle.getString(VIDEO_DATA)
        if (TextUtils.isEmpty(values)) {
            return null
        }
        try {
            return JSONObject(values)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getVideoNotificationData", e)
        }

        return null
    }

    private fun getCarouselList(extras: Bundle): ArrayList<Carousel>? {
        val carouselData = extras.getString(CAROUSEL_DATA)
        if (TextUtils.isEmpty(carouselData)) {
            return extras.getParcelableArrayList(CMConstant.ReceiverExtraData.CAROUSEL_DATA)
        }
        try {
            val listType = object : TypeToken<ArrayList<Carousel>>() {
            }.type
            return Gson().fromJson<ArrayList<Carousel>>(carouselData, listType)
        } catch (e: Exception) {
            Log.e(TAG, "CM-getCarouselList", e)
        }

        return null
    }

    private fun getPayloadExtras(data: Bundle): PayloadExtra {
        return PayloadExtra(
            campaignName = data.getString(PayloadExtraDataKey.CAMPAIGN_NAME, null),
            journeyId = data.getString(PayloadExtraDataKey.JOURNEY_ID, null),
            journeyName = data.getString(PayloadExtraDataKey.JOURNEY_NAME, null),
            sessionId = data.getString(PayloadExtraDataKey.SESSION_ID, null),
            intentAction = data.getString(PayloadExtraDataKey.INTENT_ACTION, null),
            topchat = getTopChatData(data)
        )
    }

    private fun getTopChatData(data: Bundle): Topchat? {
        return try {
            val topChatDataString = data.getString(PayloadExtraDataKey.TOPCHAT)
            Gson().fromJson(topChatDataString, Topchat::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getPayloadExtras(data: SerializedNotificationData): PayloadExtra {
        return PayloadExtra(
            campaignName = data.campaignName,
            journeyId = data.journeyId,
            journeyName = data.journeyName,
            sessionId = data.sessionId
        )
    }
}
