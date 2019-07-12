package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 *
parcel.createTypedArrayList(Carousel.CREATOR),
 * Created by Ashwani Tyagi on 18/10/18.
 */
data class BaseNotificationModel (

    var notificationId: Int = 0,
    var campaignId: Long = 0,
    var priorityPreOreo: Int = 2,
    var title: String? = null,
    var detailMessage: String? = null,
    var message: String? = null,
    var icon: String? = null,
    var soundFileName: String? = null,

    var tribeKey: String? = null,

    var media: Media? = null,

    var appLink: String? = null,
    var actionButton: List<ActionButton> = ArrayList(),
    var customValues: JSONObject? = null,
    var type: String? = null,

    var channelName: String? = null,

    var persistentButtonList: List<PersistentButton>? = null,

    var videoPushModel: JSONObject? = null,


    var subText: String? = null,

    var visualCollapsedImageUrl: String? = null,
    var visualExpandedImageUrl: String? = null,

    var carouselIndex: Int = 0,
    var isVibration: Boolean = true,
    var isSound: Boolean = true,
    var isUpdateExisting: Boolean = false,

    var carouselList: List<Carousel> = ArrayList(),
    var gridList: List<Grid> = ArrayList(),
    var productInfoList : List<ProductInfo>? = ArrayList()

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
            null,
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
            parcel.createTypedArrayList(ProductInfo))

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
