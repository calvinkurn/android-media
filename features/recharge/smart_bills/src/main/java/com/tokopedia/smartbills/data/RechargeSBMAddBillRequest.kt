package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeSBMAddBillRequest(
        @SerializedName("ProductID")
        @Expose
        val productId : Int = 0,
        @SerializedName("ClientNumber")
        @Expose
        val clientNumber : String = ""
)