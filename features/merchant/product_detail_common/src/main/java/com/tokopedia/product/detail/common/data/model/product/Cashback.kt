package com.tokopedia.product.detail.common.data.model.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cashback(
        @SerializedName("percentage")
        @Expose
        val percentage: Int = 0
)

data class PreOrder(
        @SerializedName("duration")
        @Expose
        val duration: Int = 0,

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("preorderInDays")
        @Expose
        val preorderInDays: Long = 0L
) {
    fun isPreOrderActive(): Boolean {
        return (isActive && duration > 0)
    }
}

data class Wholesale(

        @SerializedName("minQty")
        @Expose
        val minQty: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Float = 0f
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(minQty)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wholesale> {
        override fun createFromParcel(parcel: Parcel): Wholesale {
            return Wholesale(parcel)
        }

        override fun newArray(size: Int): Array<Wholesale?> {
            return arrayOfNulls(size)
        }
    }
}