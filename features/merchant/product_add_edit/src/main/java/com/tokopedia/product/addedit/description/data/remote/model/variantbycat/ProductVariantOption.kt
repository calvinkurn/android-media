package com.tokopedia.product.addedit.description.data.remote.model.variantbycat

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 8/14/2017.
 */

class ProductVariantOption private constructor(data: Parcel) : Parcelable {

    @SerializedName("value_id")
    @Expose
    var valueId: Int = 0
        private set
    @SerializedName("value")
    @Expose
    var itemId: String? = null
        private set
    @SerializedName("hex_code")
    @Expose
    var hexCode: String? = null
        private set
    @SerializedName("icon")
    @Expose
    var icon: String? = null
        private set

    val id: String
        get() = valueId.toString()

    val type: Int
        get() = TYPE

    fun getValue(): String? {
        return itemId
    }

    // this is for alias for picker type
    fun getTitle(): String? {
        return itemId
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.valueId)
        dest.writeString(this.itemId)
        dest.writeString(this.hexCode)
        dest.writeString(this.icon)
    }

    init {
        this.valueId = data.readInt()
        this.itemId = data.readString()
        this.hexCode = data.readString()
        this.icon = data.readString()
    }

    companion object CREATOR : Parcelable.Creator<ProductVariantOption> {
        val TYPE = 199349

        override fun createFromParcel(parcel: Parcel): ProductVariantOption {
            return ProductVariantOption(parcel)
        }

        override fun newArray(size: Int): Array<ProductVariantOption?> {
            return arrayOfNulls(size)
        }
    }
}
