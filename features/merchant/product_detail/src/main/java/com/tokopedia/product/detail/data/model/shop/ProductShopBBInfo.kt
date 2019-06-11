package com.tokopedia.product.detail.data.model.shop

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class ProductShopBBInfo(
        val name: String = "",
        val desc: String = "",
        val nameEN: String = "",
        val descEN: String = ""
): BlackBoxShipmentHolder(), Parcelable {

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(nameEN)
        parcel.writeString(descEN)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductShopBBInfo> {
        override fun createFromParcel(parcel: Parcel): ProductShopBBInfo {
            return ProductShopBBInfo(parcel)
        }

        override fun newArray(size: Int): Array<ProductShopBBInfo?> {
            return arrayOfNulls(size)
        }
    }
}