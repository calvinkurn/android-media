package com.tokopedia.common_category.data.catalogModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.factory.catalog.CatalogTypeFactory


data class CatalogItem(

        @field:SerializedName("price_min_raw")
        val priceMinRaw: Int? = null,

        @field:SerializedName("image_uri")
        val imageUri: String? = null,

        @field:SerializedName("department_id")
        val departmentId: Int? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("count_product")
        val countProduct: Int? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("id")
        var id: Int? = null,

        @field:SerializedName("price_min")
        val priceMin: String? = null,

        @field:SerializedName("uri")
        val uri: String? = null
) : Parcelable, Visitable<CatalogTypeFactory> {
    override fun type(typeFactory: CatalogTypeFactory?): Int {
        return typeFactory!!.type(this)
    }

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(priceMinRaw)
        parcel.writeString(imageUri)
        parcel.writeValue(departmentId)
        parcel.writeString(name)
        parcel.writeValue(countProduct)
        parcel.writeString(description)
        parcel.writeValue(id)
        parcel.writeString(priceMin)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatalogItem> {
        override fun createFromParcel(parcel: Parcel): CatalogItem {
            return CatalogItem(parcel)
        }

        override fun newArray(size: Int): Array<CatalogItem?> {
            return arrayOfNulls(size)
        }
    }
}