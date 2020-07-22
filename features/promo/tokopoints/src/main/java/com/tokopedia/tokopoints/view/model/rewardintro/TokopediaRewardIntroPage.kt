package com.tokopedia.tokopoints.view.model.rewardintro

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TokopediaRewardIntroPage(

        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null,

        @SerializedName("CTA")
        val cTA: List<CTAItem?>? = null,

        @SerializedName("subtitle")
        val subtitle: String? = null,

        @SerializedName("imageURL")
        val imageURL: String? = null,

        @SerializedName("header")
        val header: String? = null,

        @SerializedName("title")
        val title: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ResultStatus::class.java.classLoader),
            parcel.createTypedArrayList(CTAItem),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(resultStatus, flags)
        parcel.writeTypedList(cTA)
        parcel.writeString(subtitle)
        parcel.writeString(imageURL)
        parcel.writeString(header)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TokopediaRewardIntroPage> {
        override fun createFromParcel(parcel: Parcel): TokopediaRewardIntroPage {
            return TokopediaRewardIntroPage(parcel)
        }

        override fun newArray(size: Int): Array<TokopediaRewardIntroPage?> {
            return arrayOfNulls(size)
        }
    }
}