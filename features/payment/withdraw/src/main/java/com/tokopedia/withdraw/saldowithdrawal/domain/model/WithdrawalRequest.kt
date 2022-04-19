package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WithdrawalRequest(
        val userId: String = "",
        val email: String = "",
        val withdrawal: Long = 0L,
        val bankAccount: BankAccount = BankAccount(),
        val isSellerWithdrawal: Boolean = false,
        val programName: String = "",
        var isJoinRekeningPremium: Boolean = false,
        var showJoinRekeningWidget: Boolean = false
) : Parcelable