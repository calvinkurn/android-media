package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlGetBankDataResponse(
        @SerializedName("GetBankListWD")
        @Expose
        var bankAccount: GqlBankListResponse
)


data class GqlBankListResponse(
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("message")
        @Expose
        val message: String? = null,
        @SerializedName("data")
        @Expose
        var bankAccountList: ArrayList<BankAccount> = arrayListOf()
)

data class BankAccount(
        @SerializedName("bankID")
        @Expose
        var bankID: Long = 0,

        @SerializedName("accountNo")
        @Expose
        var accountNo: String? = null,

        @SerializedName("bankName")
        @Expose
        var bankName: String? = null,

        @SerializedName("bankAccountID")
        @Expose
        var bankAccountID: Long = 0,

        @SerializedName("minAmount")
        @Expose
        var minAmount: Long = 0,

        @SerializedName("maxAmount")
        @Expose
        var maxAmount: Long = 0,

        @SerializedName("adminFee")
        @Expose
        var adminFee: Long = 0,

        @SerializedName("status")
        @Expose
        var status: Int = 0,

        @SerializedName("isVerifiedAccount")
        @Expose
        var isVerifiedAccount: Long = 0,

        @SerializedName("bankImageUrl")
        @Expose
        var bankImageUrl: String? = null,

        @SerializedName("isDefaultBank")
        @Expose
        var isDefaultBank: Int = 0,

        @SerializedName("accountName")
        @Expose
        var accountName: String? = null,
        @SerializedName("is_fraud")
        @Expose
        var isFraud: Boolean = false,

        @SerializedName("have_rp_program")
        @Expose
        var haveRPProgram: Boolean = false,

        @SerializedName("have_special_offer")
        @Expose
        var haveSpecialOffer: Boolean = false,

        @SerializedName("default_bank_account")
        @Expose
        var defaultBankAccount: Boolean = false,

        @SerializedName("warning_message")
        @Expose
        var warningMessage: String? = null,

        @SerializedName("warning_color")
        @Expose
        var warningColor: Int = 0,

        var isChecked: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bankID)
        parcel.writeString(accountNo)
        parcel.writeString(bankName)
        parcel.writeLong(bankAccountID)
        parcel.writeLong(minAmount)
        parcel.writeLong(maxAmount)
        parcel.writeLong(adminFee)
        parcel.writeInt(status)
        parcel.writeLong(isVerifiedAccount)
        parcel.writeString(bankImageUrl)
        parcel.writeInt(isDefaultBank)
        parcel.writeString(accountName)
        parcel.writeByte(if (isFraud) 1 else 0)
        parcel.writeByte(if (haveRPProgram) 1 else 0)
        parcel.writeByte(if (haveSpecialOffer) 1 else 0)
        parcel.writeByte(if (defaultBankAccount) 1 else 0)
        parcel.writeString(warningMessage)
        parcel.writeInt(warningColor)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BankAccount> {
        override fun createFromParcel(parcel: Parcel): BankAccount {
            return BankAccount(parcel)
        }

        override fun newArray(size: Int): Array<BankAccount?> {
            return arrayOfNulls(size)
        }
    }
}