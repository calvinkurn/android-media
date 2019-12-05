package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.annotation.NonNull
import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.notifications.database.convertors.NotificationModeConverter
import com.tokopedia.notifications.database.convertors.NotificationStatusConverter
import org.json.JSONObject

/**
 *
parcel.createTypedArrayList(Carousel.CREATOR),
 * Created by Ashwani Tyagi on 18/10/18.
 */
@Entity(tableName = "BaseNotificationModel")
data class BaseNotificationModel(

        @ColumnInfo(name = "notificationId")
        var notificationId: Int = 0,
        
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
        var videoPushModel: JSONObject? = null,

        @ColumnInfo(name = "subText")
        var subText: String? = null,

        @ColumnInfo(name = "visualCollapsedImg")
        var visualCollapsedImageUrl: String? = null,

        @ColumnInfo(name = "visualExpandedImg")
        var visualExpandedImageUrl: String? = null,

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


        //new Fields for offline
        @ColumnInfo(name = "notificationStatus")
        var status: NotificationStatus = NotificationStatus.PENDING,

        @ColumnInfo(name = "startTime")
        var startTime: Long = 0,

        @ColumnInfo(name = "endTime")
        var endTime: Long = 0,

        @ColumnInfo(name = "notificationMode")
        var notificationMode: NotificationMode = NotificationMode.OFFLINE
        //new Fields for offline ends


) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Media::class.java.classLoader),
            parcel.readString(),
            parcel.createTypedArrayList(ActionButton),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(PersistentButton),
            null,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(Carousel),
            parcel.createTypedArrayList(Grid),
            parcel.createTypedArrayList(ProductInfo),
            parcel.readLong(),
            parcel.readString(),
            NotificationStatusConverter.instances.toStatus(parcel.readInt()),
            parcel.readLong(),
            parcel.readLong(),
            NotificationModeConverter.instances.toMode(parcel.readInt()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(notificationId)
        parcel.writeLong(campaignId)
        parcel.writeInt(priorityPreOreo)
        parcel.writeString(title)
        parcel.writeString(detailMessage)
        parcel.writeString(message)
        parcel.writeString(icon)
        parcel.writeString(soundFileName)
        parcel.writeString(tribeKey)
        parcel.writeParcelable(media, flags)
        parcel.writeString(appLink)
        parcel.writeTypedList(actionButton)
        parcel.writeString(customValues)
        parcel.writeString(type)
        parcel.writeString(channelName)
        parcel.writeTypedList(persistentButtonList)
        parcel.writeString(subText)
        parcel.writeString(visualCollapsedImageUrl)
        parcel.writeString(visualExpandedImageUrl)
        parcel.writeInt(carouselIndex)
        parcel.writeByte(if (isVibration) 1 else 0)
        parcel.writeByte(if (isSound) 1 else 0)
        parcel.writeByte(if (isUpdateExisting) 1 else 0)
        parcel.writeTypedList(carouselList)
        parcel.writeTypedList(gridList)
        parcel.writeTypedList(productInfoList)
        parcel.writeLong(parentId)
        parcel.writeString(campaignUserToken)
        NotificationStatusConverter.instances.toStatus(status.statusInt)
        parcel.writeLong(startTime)
        parcel.writeLong(endTime)
        NotificationModeConverter.instances.toMode(status.statusInt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseNotificationModel> {
        override fun createFromParcel(parcel: Parcel): BaseNotificationModel {
            return BaseNotificationModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseNotificationModel?> {
            return arrayOfNulls(size)
        }
    }

}