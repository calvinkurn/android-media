package com.tokopedia.notifications.model


import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class Media(
        @SerializedName("fallback_url")
        @ColumnInfo(name = "fallback_url")
        @Expose
        var fallbackUrl: String,

        @SerializedName("high_quality_url")
        @ColumnInfo(name = "high_quality_url")
        @Expose
        var highQuality: String,

        @SerializedName("medium_quality_url")
        @ColumnInfo(name = "medium_quality_url")
        @Expose
        var mediumQuality: String,

        @SerializedName("low_quality_url")
        @ColumnInfo(name = "low_quality_url")
        @Expose
        var lowQuality: String,

        @SerializedName("display_url")
        @ColumnInfo(name = "display_url")
        @Expose
        var displayUrl: String,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
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
