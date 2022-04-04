package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PayloadExtra(
    @SerializedName("isReview_Notif")
    @ColumnInfo(name = "isReview_Notif")
    @Expose
    var isReviewNotif: Boolean? = false,

) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readByte() != 0.toByte()) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeByte((if (isReviewNotif == true) 1 else 0).toByte())
    }

    companion object CREATOR : Parcelable.Creator<PayloadExtra> {
        override fun createFromParcel(parcel: Parcel): PayloadExtra {
            return PayloadExtra(parcel)
        }

        override fun newArray(size: Int): Array<PayloadExtra?> {
            return arrayOfNulls(size)
        }
    }
}