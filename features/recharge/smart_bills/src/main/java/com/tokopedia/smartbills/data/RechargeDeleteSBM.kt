package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeDeleteSBM(
        @SerializedName("rechargeSBMDeleteBill")
        @Expose
        val rechargeSBMDeleteBill: RechargeSBMDeleteBill = RechargeSBMDeleteBill()
)

class RechargeSBMDeleteBill(
        @SerializedName("Message")
        @Expose
        val message : String = ""
)