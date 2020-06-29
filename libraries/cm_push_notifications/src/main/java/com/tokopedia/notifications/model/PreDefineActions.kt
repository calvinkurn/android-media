package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
data class PreDefineActions(
        @SerializedName(CMConstant.PayloadKeys.TYPE)
        var type: String? = null,

        @SerializedName(CMConstant.PayloadKeys.TITLE)
        var title: String? = null,

        @SerializedName(CMConstant.PayloadKeys.MESSAGE)
        var msg: String? = null,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var element_id: String? = "",

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var genericLink: String? = "",

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var productId: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(msg)
        parcel.writeString(element_id)
        parcel.writeString(genericLink)
        parcel.writeInt(productId?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PreDefineActions> {
        override fun createFromParcel(parcel: Parcel): PreDefineActions {
            return PreDefineActions(parcel)
        }

        override fun newArray(size: Int): Array<PreDefineActions?> {
            return arrayOfNulls(size)
        }
    }

}
