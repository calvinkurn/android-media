package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowsePopularBrandsViewModel() : Parcelable, Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {

    var id: Long = 0
    var name: String? = null
    var isNew: Boolean = false
    var logoUrl: String? = null
    var url: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        isNew = parcel.readByte() != 0.toByte()
        logoUrl = parcel.readString()
        url = parcel.readString()
    }


    override fun type(typeFactory: DigitalBrowseMarketplaceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeByte(if (isNew) 1 else 0)
        parcel.writeString(logoUrl)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalBrowsePopularBrandsViewModel> {
        override fun createFromParcel(parcel: Parcel): DigitalBrowsePopularBrandsViewModel {
            return DigitalBrowsePopularBrandsViewModel(parcel)
        }

        override fun newArray(size: Int): Array<DigitalBrowsePopularBrandsViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
