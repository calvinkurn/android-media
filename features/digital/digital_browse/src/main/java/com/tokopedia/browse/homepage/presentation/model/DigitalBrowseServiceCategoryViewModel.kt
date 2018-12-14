package com.tokopedia.browse.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseServiceCategoryViewModel(val id: Int = 0,
                                            val name: String? = null,
                                            val url: String? = null,
                                            val imageUrl: String? = null,
                                            val type: String? = null,
                                            val categoryId: Int = 0,
                                            val appLinks: String? = null,
                                            val categoryLabel: String? = null,
                                            val isTitle: Boolean = false) :
        Visitable<DigitalBrowseServiceAdapterTypeFactory>, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

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
