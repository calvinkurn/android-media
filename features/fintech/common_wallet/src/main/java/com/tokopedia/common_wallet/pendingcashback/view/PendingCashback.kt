package com.tokopedia.common_wallet.pendingcashback.view

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 2/7/18.
 */
@Parcelize
class PendingCashback(
        var amount: Int = 0,
        var amountText: String = "")
    :Parcelable
