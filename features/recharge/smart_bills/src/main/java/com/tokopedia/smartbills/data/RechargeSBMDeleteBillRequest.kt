package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeSBMDeleteBillRequest(
        @SerializedName("UUID")
        @Expose
        val uuid : String = "",
        @SerializedName("Source")
        @Expose
        val source : Int = 0,
)