package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopupBillsFavNumberData(
        @SerializedName("recharge_favorite_number")
        @Expose
        val favNumber: TopupBillsFavNumber)