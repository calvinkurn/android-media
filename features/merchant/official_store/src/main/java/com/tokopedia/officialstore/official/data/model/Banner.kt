package com.tokopedia.officialstore.official.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Banner(@SerializedName("id")
                  val bannerId: String = "",
                  @SerializedName("title")
                  val title: String = "",
                  @SerializedName("image_url")
                  val imageUrl: String = "",
                  @SerializedName("topads_view_url")
                  val topadsViewUrl: String = "",
                  @SerializedName("redirect_url")
                  val redirectUrl: String = "",
                  @SerializedName("applink")
                  val applink: String = "",
                  @SerializedName("galaxy_attribution")
                  val galaxyAttribution: String = "",
                  @SerializedName("persona")
                  val persona: String = "",
                  @SerializedName("category_persona")
                  val categoryPersona: String = "",
                  @SerializedName("brand_id")
                  val brandId: String = "")
    : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bannerId)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(topadsViewUrl)
        parcel.writeString(redirectUrl)
        parcel.writeString(applink)
        parcel.writeString(persona)
        parcel.writeString(brandId)
        parcel.writeString(galaxyAttribution)
        parcel.writeString(categoryPersona)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Banner> {
        override fun createFromParcel(parcel: Parcel): Banner {
            return Banner(parcel)
        }

        override fun newArray(size: Int): Array<Banner?> {
            return arrayOfNulls(size)
        }
    }

}