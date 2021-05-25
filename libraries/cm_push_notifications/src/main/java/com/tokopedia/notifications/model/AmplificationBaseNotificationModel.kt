package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.model.WebHookParams.Companion.webHookToJson
import com.tokopedia.notifications.common.CMConstant.PayloadKeys.*
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.json.JSONObject

/**
 *
parcel.createTypedArrayList(Carousel.CREATOR),
 * Created by Ashwani Tyagi on 18/10/18.
 */
@Parcelize
data class AmplificationBaseNotificationModel(

        @Expose
        @SerializedName(NOTIFICATION_ID)
        @ColumnInfo(name = "notificationId")
        var notificationId: Int? = 0,

        @Expose
        @SerializedName(ELEMENT_ID)
        @ColumnInfo(name = "elementId")
        var elementId: String? = "",

        @Expose
        @SerializedName(CAMPAIGN_ID)
        @PrimaryKey
        @ColumnInfo(name = "campaignId")
        var campaignId: Long? = 0,

        @Expose
        @SerializedName(NOTIFICATION_PRIORITY)
        @ColumnInfo(name = "priority")
        var priorityPreOreo: Int? = 2,

        @Expose
        @SerializedName(TITLE)
        @ColumnInfo(name = "title")
        var title: String? = null,

        @Expose
        @SerializedName(DESCRIPTION)
        @ColumnInfo(name = "detailMessage")
        var detailMessage: String? = null,

        @Expose
        @SerializedName(MESSAGE)
        @ColumnInfo(name = "message")
        var message: String? = null,

        @Expose
        @SerializedName(ICON)
        @ColumnInfo(name = "icon")
        var icon: String? = null,

        @Expose
        @SerializedName(SOUND)
        @ColumnInfo(name = "soundFileName")
        var soundFileName: String? = null,

        @Expose
        @SerializedName(TRIBE_KEY)
        @ColumnInfo(name = "tribeKey")
        var tribeKey: String? = null,

        @Expose
        @SerializedName(MEDIA)
        @Embedded(prefix = "media_")
        var media: Media? = null,

        @Expose
        @SerializedName(APP_LINK)
        @ColumnInfo(name = "appLink")
        var appLink: String? = null,

        @Expose
        @SerializedName(ACTION_BUTTON)
        @ColumnInfo(name = "actionBtn")
        var actionButton: ArrayList<ActionButton?>? = ArrayList(),

        @Expose
        @SerializedName(CUSTOM_VALUE)
        @ColumnInfo(name = "customValues")
        var customValues: String? = null,


        @Expose
        @SerializedName(NOTIFICATION_TYPE)
        @ColumnInfo(name = "type")
        var type: String? = null,

        @Expose
        @SerializedName(CHANNEL)
        @ColumnInfo(name = "channelName")
        var channelName: String? = null,

        @Expose
        @SerializedName(PERSISTENT_DATA)
        @Embedded(prefix = "persist")
        var persistentButtonList: ArrayList<PersistentButton?>? = null,

        @Expose
        @SerializedName(VIDEO_DATA)
        @ColumnInfo(name = "videoPush")
        var videoPushModel: String? = null,

        @Expose
        @SerializedName(SUB_TEXT)
        @ColumnInfo(name = "subText")
        var subText: String? = null,

        @Expose
        @SerializedName(VISUAL_EXPANDED_IMAGE)
        @ColumnInfo(name = "visualCollapsedImg")
        var visualCollapsedImageUrl: String? = null,

        @Expose
        @SerializedName(VISUAL_COLLAPSED_IMAGE)
        @ColumnInfo(name = "visualExpandedImg")
        var visualExpandedImageUrl: String? = null,

        @Expose
        @SerializedName(VISUAL_COLLAPSED_ELEMENT_ID)
        @ColumnInfo(name = "visualCollapsedElementId")
        var visualCollapsedElementId: String? = "",

        @Expose
        @SerializedName(VISUAL_EXPANDED_ELEMENT_ID)
        @ColumnInfo(name = "visualExpandedElementId")
        var visualExpandedElementId: String? = "",

        @Expose
        @SerializedName(CAROUSEL_INDEX)
        @ColumnInfo(name = "carouselIndex")
        var carouselIndex: Int? = 0,

        @Expose
        @SerializedName(VIBRATE)
        @ColumnInfo(name = "isVibration")
        var isVibration: Boolean? = true,

        @Expose
        @SerializedName(UPDATE_NOTIFICATION)
        @ColumnInfo(name = "isUpdatingExisting")
        var isUpdateExisting: Boolean? = false,

        @Expose
        @SerializedName(CAROUSEL_DATA)
        @ColumnInfo(name = "carousel")
        var carouselList: ArrayList<Carousel?>? = ArrayList(),

        @Expose
        @SerializedName(GRID_DATA)
        @ColumnInfo(name = "grid")
        var gridList: ArrayList<Grid?>? = ArrayList(),

        @Expose
        @SerializedName(PRODUCT_INFO_LIST)
        @ColumnInfo(name = "productInfo")
        var productInfoList: ArrayList<ProductInfo?>? = ArrayList(),

        @Expose
        @SerializedName(PARENT_ID)
        @ColumnInfo(name = "parentId")
        var parentId: Long? = 0,

        @Expose
        @SerializedName(CAMPAIGN_USER_TOKEN)
        @ColumnInfo(name = "campaignUserToken")
        var campaignUserToken: String? = null,

        @Expose
        @SerializedName(NOTIFICATION_START_TIME)
        @ColumnInfo(name = "startTime")
        var startTime: String? = null,

        @Expose
        @SerializedName(NOTIFICATION_END_TIME)
        @ColumnInfo(name = "endTime")
        var endTime: String? = null,

        @Expose
        @SerializedName(NOTIFICATION_MODE)
        @ColumnInfo(name = "notificationMode")
        var notificationMode: Boolean?,

        @Expose
        @SerializedName(IS_TEST)
        @ColumnInfo(name = "is_test")
        var isTest: Boolean? = false,

        @Expose
        @SerializedName(TRANSACTION_ID)
        @ColumnInfo(name = "transId")
        var transactionId: String? = null,

        @Expose
        @SerializedName(USER_TRANSACTION_ID)
        @ColumnInfo(name = "userTransId")
        var userTransactionId: String? = null,

        @Expose
        @SerializedName(USER_ID)
        @ColumnInfo(name = "userId")
        var userId: String? = null,

        @Expose
        @SerializedName(SHOP_ID)
        @ColumnInfo(name = "shopId")
        var shopId: String? = null,

        @Expose
        @SerializedName(BLAST_ID)
        @ColumnInfo(name = "notifcenterBlastId")
        var blastId: String? = null,

        @Expose
        @SerializedName(WEBHOOK_PARAM)
        @ColumnInfo(name = "webhook_params")
        var webHookParam: String? = null,

        @Expose
        @SerializedName(NOTIFICATION_PRODUCT_TYPE)
        @ColumnInfo(name = "notificationProductType")
        var notificationProductType: String? = null,

        @SerializedName("is_amplification")
        @ColumnInfo(name = "is_amplification")
        var isAmplification: Boolean? = false

) : Parcelable {

    fun webHookParamData(): String? {
        return webHookToJson(this.webHookParam)
    }

}