package com.tokopedia.affiliate.feature.explore.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by yfsx on 28/12/18.
 */
data class SortFilterModel(var filterList: List<FilterViewModel> = arrayListOf(),
                           var sortList: List<SortViewModel> = arrayListOf()) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(FilterViewModel.CREATOR) ?: arrayListOf(),
            source.createTypedArrayList(SortViewModel.CREATOR) ?: arrayListOf()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(filterList)
        writeTypedList(sortList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SortFilterModel> = object : Parcelable.Creator<SortFilterModel> {
            override fun createFromParcel(source: Parcel): SortFilterModel = SortFilterModel(source)
            override fun newArray(size: Int): Array<SortFilterModel?> = arrayOfNulls(size)
        }
    }
}
