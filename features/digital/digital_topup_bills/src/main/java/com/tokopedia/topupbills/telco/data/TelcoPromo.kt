package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TelcoPromo(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("img_url")
        @Expose
        val urlBannerPromo: String,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("subtitle")
        @Expose
        val subtitle: String,
        @SerializedName("promo_code")
        @Expose
        val promoCode: String,
        var voucherCodeCopied: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(urlBannerPromo)
        parcel.writeString(title)
        parcel.writeString(subtitle)
        parcel.writeString(promoCode)
        parcel.writeByte(if (voucherCodeCopied) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoPromo> {
        override fun createFromParcel(parcel: Parcel): TelcoPromo {
            return TelcoPromo(parcel)
        }

        override fun newArray(size: Int): Array<TelcoPromo?> {
            return arrayOfNulls(size)
        }
    }

}