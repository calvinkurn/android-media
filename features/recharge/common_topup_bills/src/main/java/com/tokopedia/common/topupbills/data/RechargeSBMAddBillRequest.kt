package com.tokopedia.common.topupbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeSBMAddBillRequest (
        @SerializedName("ProductID")
        @Expose
        val productId : Int = 0,
        @SerializedName("ClientNumber")
        @Expose
        val clientNumber : String = ""
)