package com.tokopedia.attachinvoice.data


import com.google.gson.annotations.SerializedName

data class ChatListInvoice(
    @SerializedName("invoiceList")
    val invoiceList: List<Invoice> = listOf()
)