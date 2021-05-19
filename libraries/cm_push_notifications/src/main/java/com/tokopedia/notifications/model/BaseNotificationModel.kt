package com.tokopedia.notifications.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tokopedia.notifications.model.WebHookParams.Companion.webHookToJson
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.json.JSONObject

/**
 *
parcel.createTypedArrayList(Carousel.CREATOR),
 * Created by Ashwani Tyagi on 18/10/18.
 */
@Parcelize
@Entity(tableName = "BaseNotificationModel")
data class BaseNotificationModel(

        @ColumnInfo(name = "notificationId")
        var notificationId: Int = 0,

        @ColumnInfo(name = "elementId")
        var elementId: String = "",

        @PrimaryKey
        @ColumnInfo(name = "campaignId")
        var campaignId: Long = 0,

        @ColumnInfo(name = "priority")
        var priorityPreOreo: Int = 2,

        @ColumnInfo(name = "title")
        var title: String? = null,

        @ColumnInfo(name = "detailMessage")
        var detailMessage: String? = null,

        @ColumnInfo(name = "message")
        var message: String? = null,

        @ColumnInfo(name = "icon")
        var icon: String? = null,

        @ColumnInfo(name = "soundFileName")
        var soundFileName: String? = null,

        @ColumnInfo(name = "tribeKey")
        var tribeKey: String? = null,

        @Embedded(prefix = "media_")
        var media: Media? = null,

        @ColumnInfo(name = "appLink")
        var appLink: String? = null,

        @ColumnInfo(name = "actionBtn")
        var actionButton: ArrayList<ActionButton> = ArrayList(),

        @ColumnInfo(name = "customValues")
        var customValues: String? = null,

        @ColumnInfo(name = "type")
        var type: String? = null,

        @ColumnInfo(name = "channelName")
        var channelName: String? = null,

        @Embedded(prefix = "persist")
        var persistentButtonList: ArrayList<PersistentButton>? = null,

        @ColumnInfo(name = "videoPush")
        var videoPushModel: @RawValue JSONObject? = null,

        @ColumnInfo(name = "subText")
        var subText: String? = null,

        @ColumnInfo(name = "visualCollapsedImg")
        var visualCollapsedImageUrl: String? = null,

        @ColumnInfo(name = "visualExpandedImg")
        var visualExpandedImageUrl: String? = null,

        @ColumnInfo(name = "visualCollapsedElementId")
        var visualCollapsedElementId: String? = "",

        @ColumnInfo(name = "visualExpandedElementId")
        var visualExpandedElementId: String? = "",

        @ColumnInfo(name = "carouselIndex")
        var carouselIndex: Int = 0,

        @ColumnInfo(name = "isVibration")
        var isVibration: Boolean = true,

        @ColumnInfo(name = "isSound")
        var isSound: Boolean = true,

        @ColumnInfo(name = "isUpdatingExisting")
        var isUpdateExisting: Boolean = false,

        @ColumnInfo(name = "carousel")
        var carouselList: ArrayList<Carousel> = ArrayList(),

        @ColumnInfo(name = "grid")
        var gridList: ArrayList<Grid> = ArrayList(),

        @ColumnInfo(name = "productInfo")
        var productInfoList: ArrayList<ProductInfo> = ArrayList(),

        @ColumnInfo(name = "parentId")
        var parentId: Long = 0,

        @ColumnInfo(name = "campaignUserToken")
        var campaignUserToken: String? = null,

        @ColumnInfo(name = "notificationStatus")
        var status: NotificationStatus = NotificationStatus.PENDING,

        @ColumnInfo(name = "startTime")
        var startTime: Long = 0,

        @ColumnInfo(name = "endTime")
        var endTime: Long = 0,

        @ColumnInfo(name = "notificationMode")
        var notificationMode: NotificationMode = NotificationMode.OFFLINE,

        @ColumnInfo(name = "is_test")
        var isTest: Boolean = false,

        @ColumnInfo(name = "transId")
        var transactionId: String? = null,

        @ColumnInfo(name = "userTransId")
        var userTransactionId: String? = null,

        @ColumnInfo(name = "userId")
        var userId: String? = null,

        @ColumnInfo(name = "shopId")
        var shopId: String? = null,

        @ColumnInfo(name = "notifcenterBlastId")
        var blastId: String? = null,

        @ColumnInfo(name = "webhook_params")
        var webHookParam: String? = null,

        @ColumnInfo(name = "notificationProductType")
        var notificationProductType: String? = null,

        @ColumnInfo(name = "is_amplification")
        var isAmplification: Boolean = false

) : Parcelable {

    fun webHookParamData(): String? {
        return webHookToJson(this.webHookParam)
    }

}