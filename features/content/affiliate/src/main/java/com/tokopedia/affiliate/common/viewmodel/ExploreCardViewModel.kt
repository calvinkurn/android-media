package com.tokopedia.affiliate.common.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by milhamj on 14/03/19.
 */
data class ExploreCardViewModel(
        val title: String = "",
        val subtitle: String = "",
        val commission: String = "",
        val imageUrl: String = "",
        val redirectLink: String = "",
        val adId: String = "",
        val productId: String = "",
        val sectionName: String = "",
        val commissionValue: Int = 0
) : Parcelable {

    var position: Int = 0

    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(subtitle)
        writeString(commission)
        writeString(imageUrl)
        writeString(redirectLink)
        writeString(adId)
        writeString(productId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ExploreCardViewModel> = object : Parcelable.Creator<ExploreCardViewModel> {
            override fun createFromParcel(source: Parcel): ExploreCardViewModel = ExploreCardViewModel(source)
            override fun newArray(size: Int): Array<ExploreCardViewModel?> = arrayOfNulls(size)
        }
    }
}