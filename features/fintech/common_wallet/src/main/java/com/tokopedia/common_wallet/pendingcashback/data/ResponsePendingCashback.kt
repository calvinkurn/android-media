package com.tokopedia.common_wallet.pendingcashback.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 01/10/18.
 */
class ResponsePendingCashback(
        @SerializedName("goalPendingBalance")
        @Expose
        val pendingCashbackEntity: PendingCashbackEntity? = null)
