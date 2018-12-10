package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseServiceCategoryViewModel() : Visitable<DigitalBrowseServiceAdapterTypeFactory>, Parcelable {

    var id: Int = 0
    var name: String? = null
    var url: String? = null
    var imageUrl: String? = null
    var type: String? = null
    var categoryId: Int = 0
    var appLinks: String? = null
    var categoryLabel: String? = null
    var isTitle: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        url = parcel.readString()
        imageUrl = parcel.readString()
        type = parcel.readString()
        categoryId = parcel.readInt()
        appLinks = parcel.readString()
        categoryLabel = parcel.readString()
        isTitle = parcel.readByte() != 0.toByte()
    }

    override fun type(typeFactory: DigitalBrowseServiceAdapterTypeFactory): Int {
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
        parcel.writeByte(if (isTitle) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalBrowseServiceCategoryViewModel> {
        override fun createFromParcel(parcel: Parcel): DigitalBrowseServiceCategoryViewModel {
            return DigitalBrowseServiceCategoryViewModel(parcel)
        }

        override fun newArray(size: Int): Array<DigitalBrowseServiceCategoryViewModel?> {
            return arrayOfNulls(size)
        }
    }

}
