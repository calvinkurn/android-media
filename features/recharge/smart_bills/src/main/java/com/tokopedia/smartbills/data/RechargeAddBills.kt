package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeAddBillsData(
        @SerializedName("rechargeSBMAddBill")
        @Expose
        val rechargeSBMAddBill: RechargeAddBill = RechargeAddBill()
)

data class RechargeAddBill(
        @SerializedName("ErrorMessage")
        @Expose
        val errorMessage: String = "",
        @SerializedName("Message")
        @Expose
        val message: String = "",
        @SerializedName("bill")
        @Expose
        val bills: RechargeBills = RechargeBills()
)