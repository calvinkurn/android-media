package com.tokopedia.search.result.shop.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.shop.presentation.typefactory.ShopListTypeFactory

data class ShopTotalCountViewModel(
        val totalShopCount: Int = 0,
        val isAdsBannerVisible: Boolean = false
): Parcelable, Visitable<ShopListTypeFactory> {

    var query: String = ""

    override fun toString(): String {
        return this.javaClass.canonicalName ?: ""
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {

        query = parcel.readString() ?: ""
    }

    override fun type(typeFactory: ShopListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(totalShopCount)
        parcel.writeByte(if (isAdsBannerVisible) 1 else 0)
        parcel.writeString(query)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopTotalCountViewModel> {
        override fun createFromParcel(parcel: Parcel): ShopTotalCountViewModel {
            return ShopTotalCountViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopTotalCountViewModel?> {
            return arrayOfNulls(size)
        }
    }
}