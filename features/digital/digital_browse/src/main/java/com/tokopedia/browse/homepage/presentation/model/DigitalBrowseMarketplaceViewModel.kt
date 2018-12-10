package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseMarketplaceViewModel() : Parcelable {

    var popularBrandsList: List<DigitalBrowsePopularBrandsViewModel>? = null
    var rowViewModelList: List<DigitalBrowseRowViewModel>? = null

    constructor(parcel: Parcel) : this() {
        popularBrandsList = parcel.createTypedArrayList(DigitalBrowsePopularBrandsViewModel.CREATOR)
        rowViewModelList = parcel.createTypedArrayList(DigitalBrowseRowViewModel.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(popularBrandsList)
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
