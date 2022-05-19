package com.tokopedia.attachinvoice.data


import com.google.gson.annotations.SerializedName

data class GetInvoiceResponse(
    @SerializedName("chatListInvoice")
    val chatListInvoice: ChatListInvoice = ChatListInvoice()
) {

    val invoices get() = chatListInvoice.invoiceList

}