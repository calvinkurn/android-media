package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import com.google.gson.annotations.SerializedName

data class BuyerDataItem(

        @SerializedName("hold_date")
        val holdDate: String? = null,

        @SerializedName("amount_fmt")
        val amountFmt: String? = null,

        @SerializedName("reason")
        val reason: String? = null,

        @SerializedName("reason_title")
        val reasonTitle: String? = null,

        @SerializedName("release_date")
        val releaseDate: String? = null,

        @SerializedName("invoice")
        val invoice: String? = null,

        @SerializedName("type")
        val type: Int? = null
) /*: Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(holdDate)
        parcel.writeString(amountFmt)
        parcel.writeString(reason)
        parcel.writeString(reasonTitle)
        parcel.writeString(releaseDate)
        parcel.writeString(invoice)
        parcel.writeValue(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BuyerDataItem> {
        override fun createFromParcel(parcel: Parcel): BuyerDataItem {
            return BuyerDataItem(parcel)
        }

        override fun newArray(size: Int): Array<BuyerDataItem?> {
            return arrayOfNulls(size)
        }
    }*/
//}
