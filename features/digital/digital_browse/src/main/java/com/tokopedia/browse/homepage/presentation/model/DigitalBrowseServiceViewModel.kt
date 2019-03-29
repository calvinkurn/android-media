package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 07/09/18.
 */

class DigitalBrowseServiceViewModel(
        val categoryViewModelList: List<DigitalBrowseServiceCategoryViewModel>?,
        var titleMap: Map<String, IndexPositionModel>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        categoryViewModelList = parcel.createTypedArrayList(DigitalBrowseServiceCategoryViewModel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(categoryViewModelList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalBrowseServiceViewModel> {
        override fun createFromParcel(parcel: Parcel): DigitalBrowseServiceViewModel {
            return DigitalBrowseServiceViewModel(parcel)
        }

        override fun newArray(size: Int): Array<DigitalBrowseServiceViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
