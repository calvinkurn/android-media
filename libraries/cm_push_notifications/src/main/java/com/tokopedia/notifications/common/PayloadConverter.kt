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
import org.json.JSONObject
import java.util.*
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
        model.soundFileName = data.getString(SOUND, "")
        model.notificationId = data.getString(NOTIFICATION_ID, "500").toIntOrZero()
        model.campaignId = data.getString(CAMPAIGN_ID, "0").toLongOrZero()
        model.parentId = data.getString(PARENT_ID, "0").toLongOrZero()
        model.tribeKey = data.getString(TRIBE_KEY, "")
        model.type = data.getString(NOTIFICATION_TYPE, "")
        model.channelName = data.getString(CHANNEL, "")
        model.title = data.getString(TITLE, "")
        model.detailMessage = data.getString(DESCRIPTION, "")
        model.message = data.getString(MESSAGE, "")
        model.media = getMedia(data)
        model.appLink = data.getString(APP_LINK, ApplinkConst.HOME)
        val actionButtonList = getActionButtons(data)
        if (actionButtonList != null)
            model.actionButton = actionButtonList
        model.persistentButtonList = getPersistentNotificationData(data)
        model.videoPushModel = getVideoNotificationData(data)//todo check
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
        if (gridList != null)
            model.gridList = gridList
        val productInfoList = getProductInfoList(data)
        if (productInfoList != null)
            model.productInfoList = productInfoList
        model.subText = data.getString(SUB_TEXT)
        model.visualCollapsedImageUrl = data.getString(VISUAL_COLLAPSED_IMAGE)
        model.visualExpandedImageUrl = data.getString(VISUAL_EXPANDED_IMAGE)
        model.campaignUserToken = data.getString(CAMPAIGN_USER_TOKEN, "")

        model.notificationMode = getNotificationMode(data)

        //start end time,
        model.startTime = dataToLong(data, NOTIFICATION_START_TIME)
        model.endTime = dataToLong(data, NOTIFICATION_END_TIME)

        if (model.notificationMode != NotificationMode.OFFLINE && (model.startTime == 0L ||
                        model.endTime == 0L)) {
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
        model.blastId = data.getString(BLAST_ID)

        // webHook parameters
        model.webHookParam = data.getString(WEBHOOK_PARAM) //todo check

        return model
    }

    fun convertToBaseModel(data: AmplificationBaseNotificationModel): BaseNotificationModel {
        val model = BaseNotificationModel()
        model.icon = data.icon
        model.priorityPreOreo = data.priorityPreOreo ?: 2
        model.soundFileName = data.soundFileName
        model.notificationId = data.notificationId ?: 500
        model.campaignId = data.campaignId ?: 0
        model.parentId = data.parentId ?: 0
        model.tribeKey = data.tribeKey
        model.type = data.type
        model.channelName = data.channelName
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
        model.videoPushModel = data.videoPushModel?.let { JSONObject(it) }
        model.customValues = data.customValues
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
        model.campaignUserToken = data.campaignUserToken

        model.notificationMode =  if (data.notificationMode == true) NotificationMode.OFFLINE else NotificationMode.POST_NOW

        //start end time,
        model.startTime = data.startTime.toLongOrZero()
        model.endTime = data.endTime.toLongOrZero()

        if (model.notificationMode != NotificationMode.OFFLINE && (model.startTime == 0L ||
                        model.endTime == 0L)) {
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
        model.blastId = data.blastId

        // webHook parameters
        model.webHookParam = data.webHookParam

        return model
    }

    fun advanceTargetNotification(bundle: Bundle): NotificationTargetPriorities {
        val mainAppPriority = bundle.getString(MAIN_APP_PRIORITY, "1")
        val sellerAppPriority = bundle.getString(SELLER_APP_PRIORITY, "1")
        val isAdvanceTarget = isBooleanTrue(bundle, ADVANCE_TARGET)

        val appPriorities = when {
            mainAppPriority.toInt() < sellerAppPriority.toInt() -> NotificationPriorityType.MainApp
            mainAppPriority.toInt() > sellerAppPriority.toInt() -> NotificationPriorityType.SellerApp
            else -> NotificationPriorityType.Both
        }

        return NotificationTargetPriorities(appPriorities, isAdvanceTarget)
    }

    private fun isBooleanTrue(data: Bundle, key: String): Boolean {
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
}