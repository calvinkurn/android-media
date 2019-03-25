package com.tokopedia.affiliate.feature.explore.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by yfsx on 28/12/18.
 */
data class FilterViewModel(
        val name: String = "",
        val image: String = "",
        val id: Int = 0,
        var isSelected: Boolean = false
) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString() ?: "",
            source.readString() ?: "",
            source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(image)
        writeInt(id)
        writeInt((if (isSelected) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FilterViewModel> = object : Parcelable.Creator<FilterViewModel> {
            override fun createFromParcel(source: Parcel): FilterViewModel = FilterViewModel(source)
            override fun newArray(size: Int): Array<FilterViewModel?> = arrayOfNulls(size)
        }
    }
}
