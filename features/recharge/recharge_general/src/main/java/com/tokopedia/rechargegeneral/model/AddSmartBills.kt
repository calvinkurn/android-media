package com.tokopedia.rechargegeneral.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AddSmartBills(
        @SerializedName("rechargeSBMAddBill")
        @Expose
        val rechargeSBMAddBill: RechargeAddBills = RechargeAddBills()
)

data class RechargeAddBills(
        @SerializedName("ErrorMessage")
        @Expose
        val errorMessage: String = "",
        @SerializedName("Message")
        @Expose
        val message: String = "",
        @SerializedName("bill")
        @Expose
        val bills: RechargeBill = RechargeBill()
)

data class RechargeBill(
        @SerializedName("UUID")
        @Expose
        val uuid: String = "",
        @SerializedName("flag")
        @Expose
        val flag: Boolean = false,
        @SerializedName("index")
        @Expose
        val index: Int = -1,
        @SerializedName("productID")
        @Expose
        val productID: Int = 0
)
