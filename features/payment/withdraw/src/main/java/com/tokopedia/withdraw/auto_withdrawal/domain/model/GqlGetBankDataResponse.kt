package com.tokopedia.withdraw.auto_withdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlGetBankDataResponse(
        @SerializedName("GetBankListWD")
        var bankAccount: GqlBankListResponse
)


data class GqlBankListResponse(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("data")
        var bankAccountList: ArrayList<BankAccount> = arrayListOf()
)

data class BankAccount(
        @SerializedName("bankID")
        var bankID: Long = 0,

        @SerializedName("accountNo")
        var accountNo: String? = null,

        @SerializedName("bankName")
        var bankName: String? = null,

        @SerializedName("bankAccountID")
        var bankAccountID: Long = 0,

        @SerializedName("minAmount")
        var minAmount: Long = 0,

        @SerializedName("maxAmount")
        var maxAmount: Long = 0,

        @SerializedName("adminFee")
        var adminFee: Long = 0,

        @SerializedName("status")
        var status: Int = 0,

        @SerializedName("isVerifiedAccount")
        var isVerifiedAccount: Long = 0,

        @SerializedName("bankImageUrl")
        var bankImageUrl: String? = null,

        @SerializedName("isDefaultBank")
        var isDefaultBank: Int = 0,

        @SerializedName("accountName")
        var accountName: String? = null,
        @SerializedName("is_fraud")
        var isFraud: Boolean = false,

        @SerializedName("have_rp_program")
        var haveRPProgram: Boolean = false,

        @SerializedName("have_special_offer")
        var haveSpecialOffer: Boolean = false,

        @SerializedName("default_bank_account")
        var defaultBankAccount: Boolean = false,
        @SerializedName("copyWriting")
        var copyWriting: String? = null,

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
        parcel.writeString(copyWriting)
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