package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author resakemal on 10/29/19.
 */

class FintechProductInfo (
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @Expose
    var subtitle: String? = null,
    @SerializedName("link_text")
    @Expose
    var textLink: String? = null,
    @SerializedName("link_url")
    @Expose
    var urlLink: String? = null,
    @SerializedName("tooltip_text")
    @Expose
    var tooltipText: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(textLink)
        parcel.writeString(urlLink)
        parcel.writeString(tooltipText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FintechProductInfo> {
        override fun createFromParcel(parcel: Parcel): FintechProductInfo {
            return FintechProductInfo(parcel)
        }

        override fun newArray(size: Int): Array<FintechProductInfo?> {
            return arrayOfNulls(size)
        }
    }
}