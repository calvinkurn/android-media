package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseMarketplaceViewModel(
        var rowViewModelList: List<DigitalBrowseRowViewModel>? = null) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(DigitalBrowseRowViewModel))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(rowViewModelList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalBrowseMarketplaceViewModel> {
        override fun createFromParcel(parcel: Parcel): DigitalBrowseMarketplaceViewModel {
            return DigitalBrowseMarketplaceViewModel(parcel)
        }

        override fun newArray(size: Int): Array<DigitalBrowseMarketplaceViewModel?> {
            return arrayOfNulls(size)
        }
    }


}
