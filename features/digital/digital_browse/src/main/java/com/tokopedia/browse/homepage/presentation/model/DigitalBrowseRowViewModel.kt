package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowseRowViewModel() : Parcelable, Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {

    var id: Int = 0
    var name: String? = null
    var url: String? = null
    var imageUrl: String? = null
    var type: String? = null
    var categoryId: Int = 0
    var appLinks: String? = null
    var categoryLabel: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        url = parcel.readString()
        imageUrl = parcel.readString()
        type = parcel.readString()
        categoryId = parcel.readInt()
        appLinks = parcel.readString()
        categoryLabel = parcel.readString()
    }

    override fun type(typeFactory: DigitalBrowseMarketplaceAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeString(imageUrl)
        parcel.writeString(type)
        parcel.writeInt(categoryId)
        parcel.writeString(appLinks)
        parcel.writeString(categoryLabel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalBrowseRowViewModel> {
        override fun createFromParcel(parcel: Parcel): DigitalBrowseRowViewModel {
            return DigitalBrowseRowViewModel(parcel)
        }

        override fun newArray(size: Int): Array<DigitalBrowseRowViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
