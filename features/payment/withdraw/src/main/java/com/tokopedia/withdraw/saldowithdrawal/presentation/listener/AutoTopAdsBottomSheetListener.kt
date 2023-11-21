package com.tokopedia.withdraw.saldowithdrawal.presentation.listener

import android.os.Parcelable

interface AutoTopAdsBottomSheetListener: Parcelable {
    val withdrawalAmount: Long
    val recommendedAmount: Long

    fun onWithdrawOriginalAmount()
    fun onWithdrawRecommendedAmount()
}
