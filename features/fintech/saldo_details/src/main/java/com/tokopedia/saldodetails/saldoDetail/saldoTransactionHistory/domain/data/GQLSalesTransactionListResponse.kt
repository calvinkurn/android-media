package com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.saldodetails.commom.ParcelableViewModel
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoDetailTransactionFactory

data class GQLSalesTransactionListResponse(
    @SerializedName("midasHistoryInvList")
    val salesTransactionListResponse: SalesTransactionListResponse
)

data class SalesTransactionListResponse(
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
    var transactionList: List<SalesTransactionDetail>
)


data class SalesTransactionDetail(
    @SerializedName("summary_id")
    var summaryID: Long,
    @SerializedName("order_id")
    val orderID: Long,
    @SerializedName("invoice")
    val invoice: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("desc")
    val description: String,
    @SerializedName("create_time")
    val createTime: String
) : ParcelableViewModel<SaldoDetailTransactionFactory> {
    override fun type(typeFactory: SaldoDetailTransactionFactory): Int {
        return typeFactory.type(this)
    }
}
class TickerDownloadFeeTransactionModel() : ParcelableViewModel<SaldoDetailTransactionFactory>{
    override fun type(typeFactory: SaldoDetailTransactionFactory): Int {
        return typeFactory.type(this)
    }
}