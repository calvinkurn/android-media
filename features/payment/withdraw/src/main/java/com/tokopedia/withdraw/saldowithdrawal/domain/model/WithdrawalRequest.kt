package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable

data class WithdrawalRequest(
        val userId: String,
        val email: String,
        val withdrawal: Long,
        val bankAccount: BankAccount,
        val isSellerWithdrawal: Boolean,
        val programName: String,
        var isJoinRekeningPremium: Boolean,
        var showJoinRekeningWidget: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readParcelable(BankAccount::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(email)
        parcel.writeLong(withdrawal)
        parcel.writeParcelable(bankAccount, flags)
        parcel.writeByte(if (isSellerWithdrawal) 1 else 0)
        parcel.writeString(programName)
        parcel.writeByte(if (isJoinRekeningPremium) 1 else 0)
        parcel.writeByte(if (showJoinRekeningWidget) 1 else 0)
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