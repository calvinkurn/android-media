package com.tokopedia.topupbills.telco.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TelcoRechargeFavNumberData(
        @SerializedName("recharge_favorite_number")
        @Expose
        val favNumber: TelcoRechargeFavNumber)