package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 5/3/17.
 */
data class Rule(
        @SerializedName("maximum_length")
        @Expose
        var maximumLength: Int = 0,

        @SerializedName("product_text")
        @Expose
        var productText: String? = null,

        @SerializedName("product_view_style")
        @Expose
        var productViewStyle: Int = 0,

        @SerializedName("show_price")
        @Expose
        var isShowPrice: Boolean = false,

        @SerializedName("enable_voucher")
        @Expose
        var isEnableVoucher: Boolean = false,

        @SerializedName("button_text")
        @Expose
        var buttonText: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(maximumLength)
        parcel.writeString(productText)
        parcel.writeInt(productViewStyle)
        parcel.writeByte(if (isShowPrice) 1 else 0)
        parcel.writeByte(if (isEnableVoucher) 1 else 0)
        parcel.writeString(buttonText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rule> {
        override fun createFromParcel(parcel: Parcel): Rule {
            return Rule(parcel)
        }

        override fun newArray(size: Int): Array<Rule?> {
            return arrayOfNulls(size)
        }
    }


}
