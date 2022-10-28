package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopupBillsUserPerso(
    @SerializedName("loyalty_status")
    @Expose
    val loyaltyStatus: String = "",
)