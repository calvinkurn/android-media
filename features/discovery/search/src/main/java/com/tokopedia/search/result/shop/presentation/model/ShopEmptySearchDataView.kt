package com.tokopedia.search.result.shop.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

internal data class ShopEmptySearchDataView(
        val sectionTitle: String = "",
        val query: String = "",
        val isFilterActive: Boolean = false
): Parcelable, Visitable<ShopListTypeFactory> {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte())

    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sectionTitle)
        parcel.writeString(query)
        parcel.writeByte(if (isFilterActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopEmptySearchDataView> {
        override fun createFromParcel(parcel: Parcel): ShopEmptySearchDataView {
            return ShopEmptySearchDataView(parcel)
        }

        override fun newArray(size: Int): Array<ShopEmptySearchDataView?> {
            return arrayOfNulls(size)
        }
    }
}