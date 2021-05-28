package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
data class PreDefineActions(
        @Expose
        @SerializedName(CMConstant.PayloadKeys.TYPE)
        var type: String? = null,

        @Expose
        @SerializedName(CMConstant.PayloadKeys.TITLE)
        var title: String? = null,

        @Expose
        @SerializedName(CMConstant.PayloadKeys.MESSAGE)
        var msg: String? = null,

        @Expose
        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var element_id: String? = "",

        @Expose
        @SerializedName(CMConstant.PayloadKeys.PRODUCT_ID)
        var productId: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
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
