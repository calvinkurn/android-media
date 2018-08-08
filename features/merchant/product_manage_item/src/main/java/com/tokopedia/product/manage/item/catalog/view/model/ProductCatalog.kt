package com.tokopedia.product.manage.item.catalog.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.item.catalog.view.adapter.ProductCatalogTypeFactory

data class ProductCatalog(var catalogId : Int = 0, var catalogName : String = "", var catalogImage : String = "")
    : Parcelable, Visitable<ProductCatalogTypeFactory>{

    override fun type(typeFactory: ProductCatalogTypeFactory): Int = typeFactory.type(this)

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(catalogId)
        parcel.writeString(catalogName)
        parcel.writeString(catalogImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCatalog> {
        override fun createFromParcel(parcel: Parcel): ProductCatalog {
            return ProductCatalog(parcel)
        }

        override fun newArray(size: Int): Array<ProductCatalog?> {
            return arrayOfNulls(size)
        }
    }
}