package com.tokopedia.withdraw.domain.model

import android.os.Parcel
import android.os.Parcelable

data class WithdrawalRequest(
        val email: String,
        val withdrawal: Long,
        val bankAccount: BankAccount,
        val isSellerWithdrawal: Boolean,
        val userId: String,
        val programName: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readParcelable(BankAccount::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeLong(withdrawal)
        parcel.writeParcelable(bankAccount, flags)
        parcel.writeByte(if (isSellerWithdrawal) 1 else 0)
        parcel.writeString(userId)
        parcel.writeString(programName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WithdrawalRequest> {
        override fun createFromParcel(parcel: Parcel): WithdrawalRequest {
            return WithdrawalRequest(parcel)
        }

        override fun newArray(size: Int): Array<WithdrawalRequest?> {
            return arrayOfNulls(size)
        }
    }
}