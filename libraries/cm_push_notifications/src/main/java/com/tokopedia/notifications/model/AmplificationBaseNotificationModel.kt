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
    var notificationId: Int? = 0,

    @Expose
    @SerializedName(ELEMENT_ID)
    var elementId: String? = "",

    @Expose
    @SerializedName(CAMPAIGN_ID)
    var campaignId: Long? = 0,

    @Expose
    @SerializedName(NOTIFICATION_PRIORITY)
    var priorityPreOreo: Int? = 2,

    @Expose
    @SerializedName(TITLE)
    var title: String? = null,

    @Expose
    @SerializedName(DESCRIPTION)
    var detailMessage: String? = null,

    @Expose
    @SerializedName(MESSAGE)
    var message: String? = null,

    @Expose
    @SerializedName(ICON)
    var icon: String? = null,

    @Expose
    @SerializedName(SOUND)
    var soundFileName: String? = null,

    @Expose
    @SerializedName(TRIBE_KEY)
    var tribeKey: String? = null,

    @Expose
    @SerializedName(MEDIA)
    var media: Media? = null,

    @Expose
    @SerializedName(APP_LINK)
    var appLink: String? = null,

    @Expose
    @SerializedName(ACTION_BUTTON)
    var actionButton: ArrayList<ActionButton?>? = ArrayList(),

    @Expose
    @SerializedName(CUSTOM_VALUE)
    var customValues: String? = null,


    @Expose
    @SerializedName(NOTIFICATION_TYPE)
    var type: String? = null,

    @Expose
    @SerializedName(CHANNEL)
    var channelName: String? = null,

    @Expose
    @SerializedName(PERSISTENT_DATA)
    var persistentButtonList: ArrayList<PersistentButton?>? = null,

    @Expose
    @SerializedName(VIDEO_DATA)
    var videoPushModel: String? = null,

    @Expose
    @SerializedName(SUB_TEXT)
    var subText: String? = null,

    @Expose
    @SerializedName(VISUAL_EXPANDED_IMAGE)
    var visualCollapsedImageUrl: String? = null,

    @Expose
    @SerializedName(VISUAL_COLLAPSED_IMAGE)
    var visualExpandedImageUrl: String? = null,

    @Expose
    @SerializedName(VISUAL_COLLAPSED_ELEMENT_ID)
    var visualCollapsedElementId: String? = "",

    @Expose
    @SerializedName(VISUAL_EXPANDED_ELEMENT_ID)
    var visualExpandedElementId: String? = "",

    @Expose
    @SerializedName(CAROUSEL_INDEX)
    var carouselIndex: Int? = 0,

    @Expose
    @SerializedName(VIBRATE)
    var isVibration: Boolean? = true,

    @Expose
    @SerializedName(UPDATE_NOTIFICATION)
    var isUpdateExisting: Boolean? = false,

    @Expose
    @SerializedName(CAROUSEL_DATA)
    var carouselList: ArrayList<Carousel?>? = ArrayList(),

    @Expose
    @SerializedName(GRID_DATA)
    var gridList: ArrayList<Grid?>? = ArrayList(),

    @Expose
    @SerializedName(PRODUCT_INFO_LIST)
    var productInfoList: ArrayList<ProductInfo?>? = ArrayList(),

    @Expose
    @SerializedName(PARENT_ID)
    var parentId: Long? = 0,

    @Expose
    @SerializedName(CAMPAIGN_USER_TOKEN)
    var campaignUserToken: String? = null,

    @Expose
    @SerializedName(NOTIFICATION_START_TIME)
    var startTime: String? = null,

    @Expose
    @SerializedName(NOTIFICATION_END_TIME)
    var endTime: String? = null,

    @Expose
    @SerializedName(NOTIFICATION_MODE)
    var notificationMode: Boolean?,

    @Expose
    @SerializedName(IS_TEST)
    var isTest: Boolean? = false,

    @Expose
    @SerializedName(TRANSACTION_ID)
    var transactionId: String? = null,

    @Expose
    @SerializedName(USER_TRANSACTION_ID)
    var userTransactionId: String? = null,

    @Expose
    @SerializedName(USER_ID)
    var userId: String? = null,

    @Expose
    @SerializedName(SHOP_ID)
    var shopId: String? = null,

    @Expose
    @SerializedName(IS_BIG_IMAGE)
    var isBigImage: Boolean? = false,

    @Expose
    @SerializedName(BLAST_ID)
    var blastId: String? = null,

    @Expose
    @SerializedName(WEBHOOK_PARAM)
    var webHookParam: String? = null,

    @Expose
    @SerializedName(NOTIFICATION_PRODUCT_TYPE)
    var notificationProductType: String? = null,

    @Expose
    @SerializedName(PayloadExtraDataKey.CAMPAIGN_NAME)
    var campaignName: String?,

    @Expose
    @SerializedName(PayloadExtraDataKey.JOURNEY_ID)
    var journeyId: String?,

    @Expose
    @SerializedName(PayloadExtraDataKey.JOURNEY_NAME)
    var journeyName: String?,

) : Parcelable {

    fun webHookParamData(): String? {
        return webHookToJson(this.webHookParam)
    }

}