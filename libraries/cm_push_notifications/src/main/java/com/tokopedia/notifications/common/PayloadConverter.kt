package com.tokopedia.notifications.common

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.notifications.model.*
import org.json.JSONObject
import java.util.*

const val HOURS_24_IN_MILLIS: Long = 24 * 60 * 60 * 1000L

object PayloadConverter {

    private val TAG = PayloadConverter::class.java.simpleName

    public fun convertMapToBundle(map: Map<String, String>?): Bundle {
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
        model.icon = data.getString(CMConstant.PayloadKeys.ICON, "")
        if (data.containsKey(CMConstant.PayloadKeys.NOTIFICATION_PRIORITY)) {
            model.priorityPreOreo = Integer.parseInt(data.getString(CMConstant.PayloadKeys.NOTIFICATION_PRIORITY, "2"))
        }
        model.soundFileName = data.getString(CMConstant.PayloadKeys.SOUND, "")
        model.notificationId = Integer.parseInt(data.getString(CMConstant.PayloadKeys.NOTIFICATION_ID, "500"))
        model.campaignId = java.lang.Long.parseLong(data.getString(CMConstant.PayloadKeys.CAMPAIGN_ID, "0"))
        model.parentId = java.lang.Long.parseLong(data.getString(CMConstant.PayloadKeys.PARENT_ID, "0"))
        model.tribeKey = data.getString(CMConstant.PayloadKeys.TRIBE_KEY, "")
        model.type = data.getString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, "")
        model.channelName = data.getString(CMConstant.PayloadKeys.CHANNEL, "")
        model.title = data.getString(CMConstant.PayloadKeys.TITLE, "")
        model.detailMessage = data.getString(CMConstant.PayloadKeys.DESCRIPTION, "")
        model.message = data.getString(CMConstant.PayloadKeys.MESSAGE, "")
        model.media = getMedia(data)
        model.appLink = data.getString(CMConstant.PayloadKeys.APP_LINK, ApplinkConst.HOME)
        val actionButtonList = getActionButtons(data)
        if (actionButtonList != null)
            model.actionButton = actionButtonList
        model.persistentButtonList = getPersistentNotificationData(data)
        model.videoPushModel = getVideoNotificationData(data)
        model.customValues = data.getString(CMConstant.PayloadKeys.CUSTOM_VALUE, "")
        val carouselList = getCarouselList(data)
        if (carouselList != null) {
            model.carouselList = carouselList
            model.carouselIndex = data.getInt(CMConstant.PayloadKeys.CAROUSEL_INDEX, 0)
        }
        model.isVibration = data.getBoolean(CMConstant.PayloadKeys.VIBRATE, true)
        model.isUpdateExisting = data.getBoolean(CMConstant.PayloadKeys.UPDATE_NOTIFICATION, false)
        val gridList = getGridList(data)
        if (gridList != null)
            model.gridList = gridList
        val productInfoList = getProductInfoList(data)
        if (productInfoList != null)
            model.productInfoList = productInfoList
        model.subText = data.getString(CMConstant.PayloadKeys.SUB_TEXT)
        model.visualCollapsedImageUrl = data.getString(CMConstant.PayloadKeys.VISUAL_COLLAPSED_IMAGE)
        model.visualExpandedImageUrl = data.getString(CMConstant.PayloadKeys.VISUAL_EXPANDED_IMAGE)
        model.campaignUserToken = data.getString(CMConstant.PayloadKeys.CAMPAIGN_USER_TOKEN, "")

        //start end time,
        model.notificationMode = getNotificationMode(data)

        model.startTime = if (data.containsKey(CMConstant.PayloadKeys.NOTIFICATION_START_TIME)) {
            try {
                data.getString(CMConstant.PayloadKeys.NOTIFICATION_START_TIME).toLong()
            } catch (e: Exception) {
                0L
            }
        } else 0L
        model.endTime = if (data.containsKey(CMConstant.PayloadKeys.NOTIFICATION_END_TIME)) {
            try {
                data.getString(CMConstant.PayloadKeys.NOTIFICATION_END_TIME).toLong()
            } catch (e: Exception) {
                0L
            }
        } else 0L
        if (model.notificationMode != NotificationMode.OFFLINE && (model.startTime == 0L ||
                        model.endTime == 0L)) {
            model.startTime = System.currentTimeMillis()
            model.endTime = System.currentTimeMillis() + HOURS_24_IN_MILLIS
        }

        model.status = NotificationStatus.PENDING

        return model
    }

    private fun getNotificationMode(data: Bundle): NotificationMode {
        try {
            if (data.containsKey(CMConstant.PayloadKeys.NOTIFICATION_MODE)) {
                return if (data.getString(CMConstant.PayloadKeys.NOTIFICATION_MODE).toBoolean()) NotificationMode.OFFLINE
                else NotificationMode.POST_NOW
            }
        } catch (e: Exception) {
        }

        return NotificationMode.POST_NOW
    }

    private fun getMedia(extras: Bundle): Media? {
        val actions = extras.getString(CMConstant.PayloadKeys.MEDIA)
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
        val actions = extras.getString(CMConstant.PayloadKeys.ACTION_BUTTON)
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
        val persistentData = bundle.getString(CMConstant.PayloadKeys.PERSISTENT_DATA)
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
        val productInfoListStr = bundle.getString(CMConstant.PayloadKeys.PRODUCT_INFO_LIST)
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
        val persistentData = bundle.getString(CMConstant.PayloadKeys.GRID_DATA)
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

        val values = bundle.getString(CMConstant.PayloadKeys.VIDEO_DATA)
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
        val carouselData = extras.getString(CMConstant.PayloadKeys.CAROUSEL_DATA)
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