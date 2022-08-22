package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import com.google.gson.annotations.SerializedName

data class ResponseGetWithdrawalInfo(
    @SerializedName("RichieGetWithdrawalInfo")
    val data: WithdrawalInfoResult
)

data class WithdrawalInfoResult(
    @SerializedName("data")
    val withdrawalData: WithdrawalInfo,
    @SerializedName("message_error")
    val error: ArrayList<String>
)
data class WithdrawalInfo(
    @SerializedName("is_success")
    val isSuccess: Int,
    @SerializedName("withdrawal_info")
    val withdrawalInfoData: WithdrawalInfoData
)

data class WithdrawalInfoData(
    @SerializedName("withdrawal_id")
    val withdrawalId: String,
    @SerializedName("status")
    val withdrawalInfoStatus: Int,
    @SerializedName("status_string")
    val labelStatus: String,
    @SerializedName("status_color")
    val labelColor: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("fee")
    val fee: Double,
    @SerializedName("transferred_amount")
    val transferredAmount: Double,
    @SerializedName("create_time")
    val createdTime: String,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("acc_no")
    val accountNumber: String,
    @SerializedName("acc_name")
    val accountName: String,
    @SerializedName("history")
    val withdrawalInfoHistory: ArrayList<WithdrawalInfoHistory>,
    var feeDetailData: ArrayList<FeeDetailData>
)

data class FeeDetailData(
    @SerializedName("type_description")
    val feeType: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("is_late")
    val isLate: Boolean,
    @SerializedName("last_update_deposit")
    val lastUpdateDetail:String?
)

data class WithdrawalInfoHistory(
    @SerializedName("title")
    val historyTitle: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("create_time")
    val createdTime: String
)
