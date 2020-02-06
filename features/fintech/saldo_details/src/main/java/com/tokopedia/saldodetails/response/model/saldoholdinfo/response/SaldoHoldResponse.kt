package com.tokopedia.saldodetails.response.model.saldoholdinfo.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SaldoHoldResponse(

        @SerializedName("saldoHoldDepositHistory")
        var saldoHoldDepositHistory: SaldoHoldDepositHistory? = null
) /*: Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable<SaldoHoldDepositHistory>(SaldoHoldDepositHistory::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(saldoHoldDepositHistory, flags)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SaldoHoldResponse> {
        override fun createFromParcel(parcel: Parcel): SaldoHoldResponse {
            return SaldoHoldResponse(parcel)
        }

        override fun newArray(size: Int): Array<SaldoHoldResponse?> {
            return arrayOfNulls(size)
        }
    }
}*/