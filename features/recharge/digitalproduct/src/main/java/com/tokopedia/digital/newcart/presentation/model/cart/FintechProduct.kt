package com.tokopedia.digital.newcart.presentation.model.cart

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class FintechProduct (
    @SerializedName("transaction_type")
    @Expose
    var transactionType: String? = null,
    @SerializedName("tier_id")
    @Expose
    var tierId: Int = 0,
    @SerializedName("opt_in")
    @Expose
    var optIn: Boolean = false,
    @SerializedName("check_box_disabled")
    @Expose
    var checkBoxDisabled: Boolean = false,
    @SerializedName("allow_ovo_points")
    @Expose
    var allowOVOPoints: Boolean = false,
    @SerializedName("fintech_amount")
    @Expose
    var fintechAmount: Long = 0,
    @SerializedName("fintech_partner_amount")
    @Expose
    var fintechPartnerAmount: Long = 0,
    @SerializedName("info")
    @Expose
    var info: FintechProductInfo? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readParcelable(FintechProductInfo::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(transactionType)
        parcel.writeInt(tierId)
        parcel.writeByte(if (optIn) 1 else 0)
        parcel.writeByte(if (checkBoxDisabled) 1 else 0)
        parcel.writeByte(if (allowOVOPoints) 1 else 0)
        parcel.writeLong(fintechAmount)
        parcel.writeLong(fintechPartnerAmount)
        parcel.writeParcelable(info, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FintechProduct> {
        override fun createFromParcel(parcel: Parcel): FintechProduct {
            return FintechProduct(parcel)
        }

        override fun newArray(size: Int): Array<FintechProduct?> {
            return arrayOfNulls(size)
        }
    }
}