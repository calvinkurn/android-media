package com.tokopedia.settingbank.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BankAccountListResponse(
        @SerializedName("GetBankAccount")
        @Expose
        val getBankAccount: GetBankAccount
)

data class GetBankAccount(
        @SerializedName("status")
        @Expose
        val status: String? = null,
        @SerializedName("header")
        @Expose
        val header: Header,
        @SerializedName("data")
        @Expose
        val data: Data
)

data class Header(
        @SerializedName("processTime")
        @Expose
        val processTime: Double,
        @SerializedName("message")
        @Expose
        val message: List<String>? = null,
        @SerializedName("reason")
        @Expose
        val reason: String? = null,
        @SerializedName("errorCode")
        @Expose
        val errorCode: String? = null
)

data class Data(
        @SerializedName("bankAccounts")
        @Expose
        val bankAccount: List<BankAccount>? = null,
        @SerializedName("userInfo")
        @Expose
        val userInfo: UserInfo
)

data class BankAccount(
        @SerializedName("accID")
        @Expose
        val accID: Long,
        @SerializedName("accName")
        @Expose
        val accName: String? = null,
        @SerializedName("accNumber")
        @Expose
        val accNumber: String? = null,
        @SerializedName("bankID")
        @Expose
        val bankID: Long,
        @SerializedName("bankName")
        @Expose
        val bankName: String? = null,
        @SerializedName("bankImageUrl")
        @Expose
        val bankImageUrl: String? = null,
        @SerializedName("statusFraud")
        @Expose
        val statusFraud: Int,
        @SerializedName("copyWriting")
        @Expose
        val copyWriting: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(accID)
        parcel.writeString(accName ?: "")
        parcel.writeString(accNumber ?: "")
        parcel.writeLong(bankID)
        parcel.writeString(bankName ?: "")
        parcel.writeString(bankImageUrl ?: "")
        parcel.writeInt(statusFraud)
        parcel.writeString(copyWriting ?: "")
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

data class UserInfo(
        @SerializedName("message")
        @Expose
        val message: String? = null,
        @SerializedName("isVerified")
        @Expose
        val isVerified: Boolean
)

