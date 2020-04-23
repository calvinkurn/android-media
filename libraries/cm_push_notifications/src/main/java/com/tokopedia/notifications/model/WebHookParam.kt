package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

data class WebHookParam(
        @Expose
        @ColumnInfo(name = CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_ID)
        @SerializedName(CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_ID)
        var notificationCenterId: String? = null,

        @Expose
        @ColumnInfo(name = CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_TYPE)
        @SerializedName(CMConstant.PayloadKeys.NOTIFCENTER_NOTIFICATION_TYPE)
        var typeOfNotification: Int? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationCenterId)
        parcel.writeValue(typeOfNotification)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebHookParam> {
        override fun createFromParcel(parcel: Parcel): WebHookParam {
            return WebHookParam(parcel)
        }

        override fun newArray(size: Int): Array<WebHookParam?> {
            return arrayOfNulls(size)
        }
    }
}