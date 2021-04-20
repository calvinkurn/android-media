package com.tokopedia.settingbank.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBankListResponse(
        @SerializedName("RichieGetBankList")
        @Expose
        val bankListResponse: BankListResponse?
)

data class BankListResponse(
        @SerializedName("status")
        @Expose
        val status: String? = null,
        @SerializedName("header")
        @Expose
        val bankListHeader: BankListHeader? = null,
        @SerializedName("data")
        @Expose
        val bankData: BankData? = null
)

data class BankListHeader(
        @SerializedName("messages")
        @Expose
        val messageList: ArrayList<String?>? = null,
        @SerializedName("reason")
        @Expose
        val reason: String? = null,
        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String? = null
)

data class BankData(
        @SerializedName("banks")
        @Expose
        val bankList: ArrayList<Bank>? = null
)

data class Bank(
        @SerializedName("bankID")
        @Expose
        val bankID: Long,
        @SerializedName("bankName")
        @Expose
        val bankName: String? = null,
        @SerializedName("clearingCode")
        @Expose
        val clearingCode: String? = null,
        @SerializedName("abbreviation")
        @Expose
        val abbreviation: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bankID)
        parcel.writeString(bankName ?: "")
        parcel.writeString(clearingCode ?: "")
        parcel.writeString(abbreviation ?: "")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bank> {
        override fun createFromParcel(parcel: Parcel): Bank {
            return Bank(parcel)
        }

        override fun newArray(size: Int): Array<Bank?> {
            return arrayOfNulls(size)
        }
    }
}

