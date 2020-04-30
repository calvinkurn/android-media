package com.tokopedia.home.beranda.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsDrawer(
    @SerializedName("redirectURL")
    val redirectURL: String = "",
    @SerializedName("iconImageURL")
    val iconImageURL: String = "",
    @SerializedName("sectionContent")
    val sectionContent: List<SectionContentItem> = listOf(),
    @SerializedName("redirectAppLink")
    val redirectAppLink: String = "",
    @Expose(serialize = false, deserialize = false)
    val mainPageTitle: String = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(SectionContentItem.CREATOR),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(redirectURL)
        parcel.writeString(iconImageURL)
        parcel.writeTypedList(sectionContent)
        parcel.writeString(redirectAppLink)
        parcel.writeString(mainPageTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TokopointsDrawer> {
        override fun createFromParcel(parcel: Parcel): TokopointsDrawer {
            return TokopointsDrawer(parcel)
        }

        override fun newArray(size: Int): Array<TokopointsDrawer?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "TokopointsDrawer{" +
                "redirectURL = '" + redirectURL + '\'' +
                ",iconImageURL = '" + iconImageURL + '\'' +
                ",sectionContent = '" + sectionContent + '\'' +
                ",redirectAppLink = '" + redirectAppLink + '\'' +
                "}"
    }
}