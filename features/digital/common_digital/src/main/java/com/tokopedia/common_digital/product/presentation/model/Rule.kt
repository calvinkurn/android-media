package com.tokopedia.common_digital.product.presentation.model

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 5/3/17.
 */
class Rule : Parcelable {

    @SerializedName("maximum_length")
    @Expose
    var maximumLength: Int = 0

    @SerializedName("product_text")
    @Expose
    var productText: String? = null

    @SerializedName("product_view_style")
    @Expose
    var productViewStyle: Int = 0

    @SerializedName("show_price")
    @Expose
    var isShowPrice: Boolean = false

    @SerializedName("enable_voucher")
    @Expose
    var isEnableVoucher: Boolean = false

    @SerializedName("button_text")
    @Expose
    var buttonText: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.maximumLength)
        dest.writeString(this.productText)
        dest.writeInt(this.productViewStyle)
        dest.writeByte(if (this.isShowPrice) 1.toByte() else 0.toByte())
        dest.writeByte(if (this.isEnableVoucher) 1.toByte() else 0.toByte())
        dest.writeString(this.buttonText)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        this.maximumLength = `in`.readInt()
        this.productText = `in`.readString()
        this.productViewStyle = `in`.readInt()
        this.isShowPrice = `in`.readByte().toInt() != 0
        this.isEnableVoucher = `in`.readByte().toInt() != 0
        this.buttonText = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Rule> {
        override fun createFromParcel(source: Parcel): Rule {
            return Rule(source)
        }

        override fun newArray(size: Int): Array<Rule?> {
            return arrayOfNulls(size)
        }

    }

}
