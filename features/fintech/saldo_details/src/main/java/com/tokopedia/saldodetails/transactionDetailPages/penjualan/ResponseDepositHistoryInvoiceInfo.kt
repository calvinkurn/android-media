package com.tokopedia.saldodetails.transactionDetailPages.penjualan

import com.google.gson.annotations.SerializedName
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.FeeDetailData

data class ResponseDepositHistoryInvoiceInfo(
    @SerializedName("MidasDepositHistoryInvoiceDetail")
    val response: DepositHistoryInvoiceDetail
)

data class DepositHistoryInvoiceDetail(
    @SerializedName("is_success")
    val isSuccess: Boolean,
    @SerializedName("deposit_history")
    val data: DepositHistoryData
)

data class DepositHistoryData(
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("create_time")
    val createdTime: String,
    @SerializedName("invoice_no")
    val invoiceNumber: String,
    @SerializedName("invoice_url")
    val invoiceUrl: String,
    @SerializedName("order_url_android")
    val orderUrl: String,
    @SerializedName("detail")
    val depositDetail: ArrayList<FeeDetailData>
)

