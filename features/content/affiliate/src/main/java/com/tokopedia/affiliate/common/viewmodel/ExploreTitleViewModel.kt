package com.tokopedia.affiliate.common.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by milhamj on 14/03/19.
 */
data class ExploreTitleViewModel(
        val title: String = "",
        val subtitle: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: ""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(subtitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ExploreTitleViewModel> = object : Parcelable.Creator<ExploreTitleViewModel> {
            override fun createFromParcel(source: Parcel): ExploreTitleViewModel = ExploreTitleViewModel(source)
            override fun newArray(size: Int): Array<ExploreTitleViewModel?> = arrayOfNulls(size)
        }
    }
}