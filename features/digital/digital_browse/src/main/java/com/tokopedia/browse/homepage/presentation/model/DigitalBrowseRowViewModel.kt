package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseMarketplaceAdapterTypeFactory

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowseRowViewModel(val id: Int, val name: String?, val url: String?,
                                val imageUrl: String?, val type: String?,
                                val categoryId: Int, val appLinks: String?,
                                val categoryLabel: String?) :
        Parcelable, Visitable<DigitalBrowseMarketplaceAdapterTypeFactory> {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

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
