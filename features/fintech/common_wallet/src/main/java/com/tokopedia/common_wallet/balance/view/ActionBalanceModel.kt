package com.tokopedia.common_wallet.balance.view

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by nabillasabbaha on 9/10/19.
 */
@Parcelize
class ActionBalanceModel(
        var redirectUrl: String = "",
        var labelAction: String = "",
        var applinks: String = "",
        var visibility: String = "")
    : Parcelable
