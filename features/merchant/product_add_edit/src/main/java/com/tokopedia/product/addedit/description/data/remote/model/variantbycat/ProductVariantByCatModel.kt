package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 8/14/2017.
 */

class ProductVariantByCatModel private constructor(data: Parcel) : Parcelable {

    @SerializedName("variant_id")
    @Expose
    var variantId: Int = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("identifier")
    @Expose
    var identifier: String?  = null
    @SerializedName("status") // 2: level 1, 1: level 2
    @Expose
    var status: Int = 0
    @SerializedName("has_unit")
    @Expose
    var hasUnit: Int = 0
    @SerializedName("units")
    @Expose
    var unitList: List<ProductVariantUnit> = emptyList()

    val level: Int
        get() = if (status == STATUS_LEVEL_1) 1 else 2

    val isDataColorType: Boolean
        get() = variantId == COLOR_ID

    val isSizeIdentifier: Boolean
        get() = identifier!!.equals(SIZE_IDENTIFIER_STRING, ignoreCase = true)

    fun hasUnit(): Boolean {
        return hasUnit > 0 && unitList.isNotEmpty()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.variantId)
        dest.writeString(this.name)
        dest.writeString(this.identifier)
        dest.writeInt(this.status)
        dest.writeInt(this.hasUnit)
        dest.writeTypedList(this.unitList)
    }


    init {
        this.variantId = data.readInt()
        this.name = data.readString()
        this.identifier = data.readString()
        this.status = data.readInt()
        this.hasUnit = data.readInt()
        this.unitList = data.createTypedArrayList(ProductVariantUnit.CREATOR) ?: emptyList()
    }

    companion object CREATOR : Parcelable.Creator<ProductVariantByCatModel> {
        const val COLOR_ID = 1 // from API

        const val STATUS_LEVEL_1 = 2 // from API
        const val SIZE_IDENTIFIER_STRING = "size" // from API

        override fun createFromParcel(parcel: Parcel): ProductVariantByCatModel {
            return ProductVariantByCatModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductVariantByCatModel?> {
            return arrayOfNulls(size)
        }
    }

}
