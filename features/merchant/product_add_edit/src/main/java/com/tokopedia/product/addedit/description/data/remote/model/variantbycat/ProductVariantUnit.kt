package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Created by hendry on 8/14/2017.
 */

class ProductVariantUnit private constructor(data: Parcel) : Parcelable {
    @SerializedName("unit_id")
    @Expose
    var unitId: Int = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("short_name")
    @Expose
    var shortName: String? = null
    @SerializedName("values")
    @Expose
    var productVariantOptionList: List<ProductVariantOption> = emptyList()

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(this.unitId)
        dest.writeString(this.name)
        dest.writeString(this.shortName)
        dest.writeList(this.productVariantOptionList)
    }

    init {
        this.unitId = data.readValue(Int::class.java.classLoader) as Int
        this.name = data.readString()
        this.shortName = data.readString()
        this.productVariantOptionList = ArrayList()
        data.readList(this.productVariantOptionList, ProductVariantOption::class.java.classLoader)
    }

    companion object CREATOR : Parcelable.Creator<ProductVariantUnit> {
        override fun createFromParcel(parcel: Parcel): ProductVariantUnit {
            return ProductVariantUnit(parcel)
        }

        override fun newArray(size: Int): Array<ProductVariantUnit?> {
            return arrayOfNulls(size)
        }
    }
}
