package com.tokopedia.sellerorder.waitingpaymentorder.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class WaitingPaymentOrderRequestParam(
        @SerializedName("is_mobile")
        @Expose
        val isMobile: Boolean = true,

        @SerializedName("next_payment_deadline")
        @Expose
        val nextPaymentDeadline: Long = 0,

        @SerializedName("lang")
        @Expose
        val lang: String = "id",

        @SerializedName("page")
        @Expose
        val page: Int = 1,

        @SerializedName("batch_page")
        @Expose
        val batchPage: Int = 1,

        @SerializedName("show_page")
        @Expose
        val showPage: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readLong(),
            parcel.readString().orEmpty(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isMobile) 1 else 0)
        parcel.writeLong(nextPaymentDeadline)
        parcel.writeString(lang)
        parcel.writeInt(page)
        parcel.writeInt(batchPage)
        parcel.writeInt(showPage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WaitingPaymentOrderRequestParam> {
        override fun createFromParcel(parcel: Parcel): WaitingPaymentOrderRequestParam {
            return WaitingPaymentOrderRequestParam(parcel)
        }

        override fun newArray(size: Int): Array<WaitingPaymentOrderRequestParam?> {
            return arrayOfNulls(size)
        }
    }
}