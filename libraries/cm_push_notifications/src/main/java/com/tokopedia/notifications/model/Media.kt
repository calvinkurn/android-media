package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class Media(
        @SerializedName("fallback_url")
        var fallbackUrl: String,

        @SerializedName("high_quality_url")
        var highQuality: String,

        @SerializedName("medium_quality_url")
        var mediumQuality: String,

        @SerializedName("low_quality_url")
        var lowQuality: String,

        @SerializedName("display_url")
        var displayUrl: String,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        var element_id: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "",
            parcel.readString()?.let { it } ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fallbackUrl)
        parcel.writeString(highQuality)
        parcel.writeString(mediumQuality)
        parcel.writeString(lowQuality)
        parcel.writeString(displayUrl)
        parcel.writeString(element_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }

}
