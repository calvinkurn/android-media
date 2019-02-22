package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 1/23/18.
 */

class ManualTransferEntity(
        @SerializedName("unique_code")
        @Expose
        val uniqueCode: Int,
        @SerializedName("total")
        @Expose
        val total: String,
        @SerializedName("account_bank_name")
        @Expose
        val accountBankName: String,
        @SerializedName("account_branch")
        @Expose
        val accountBranch: String,
        @SerializedName("account_name")
        @Expose
        val accountName: String,
        @SerializedName("account_no")
        @Expose
        val accountNo: String)
