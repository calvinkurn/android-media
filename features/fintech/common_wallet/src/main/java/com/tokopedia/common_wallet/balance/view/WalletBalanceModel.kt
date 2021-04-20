package com.tokopedia.common_wallet.balance.view

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 9/10/19.
 */
@Parcelize
class WalletBalanceModel(
        var titleText: String = "",
        var actionBalanceModel: ActionBalanceModel? = null,
        var balance: String = "",
        var rawBalance: Long = 0,
        var totalBalance: String = "",
        var rawTotalBalance: Long = 0,
        var holdBalance: String = "",
        var rawHoldBalance: Long = 0,
        var applinks: String = "",
        var redirectUrl: String = "",
        var link: Boolean = false,
        val rawThreshold: Long = 0,
        val threshold: String = "",
        var abTags: List<String>? = null,
        var pointBalance: String = "",
        var rawPointBalance: Int = 0,
        var cashBalance: String = "",
        var rawCashBalance: Int = 0,
        var walletType: String = "",
        var pendingCashback: String = "",
        var amountPendingCashback: Int = 0,
        var helpApplink: String = "",
        var tncApplink: String = "",
        var isShowAnnouncement: Boolean = false,
        var isShowTopup: Boolean = false,
        var topupUrl: String = "",
        var topupLimit: Long = 0,
        var isError: Boolean = false,
        var errorMessage: String = "")

    : Parcelable