package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class Grid(
        @SerializedName("appLink")
        var appLink: String? = null,

        @SerializedName("img")
        var img: String? = null,
        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var element_id: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appLink)
        parcel.writeString(img)
        parcel.writeString(element_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grid> {
        override fun createFromParcel(parcel: Parcel): Grid {
            return Grid(parcel)
        }

        override fun newArray(size: Int): Array<Grid?> {
            return arrayOfNulls(size)
        }
    }

}
