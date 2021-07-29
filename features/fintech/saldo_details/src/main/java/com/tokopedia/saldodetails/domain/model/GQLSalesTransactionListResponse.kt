package com.tokopedia.saldodetails.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.saldodetails.response.model.DepositHistoryList

data class GQLSalesTransactionListResponse (
    @SerializedName("message_status")
    var messageStatus: String,

    @SerializedName("public_message_desc")
    var publicMessageDescription: String,

    @SerializedName("public_message_title")
    var publicMessageTitle: String,

    @SerializedName("is_success")
    var isSuccess: Int,

    @SerializedName("have_next_page")
    var isHaveNextPage: Boolean = false,

    @SerializedName("content")
    var transactionList: List<DepositHistoryList>)

data class SalesTransactionDetail(
    @SerializedName("summary_id")
    var summaryID: Long,
    @SerializedName("order_id")
    val orderID : Long,
    @SerializedName("invoice")
    val invoice : String,
    @SerializedName("amount")
    val amount : Double,
    @SerializedName("desc")
    val desc : Double,
    @SerializedName("create_time")
    val createTime : String
)
