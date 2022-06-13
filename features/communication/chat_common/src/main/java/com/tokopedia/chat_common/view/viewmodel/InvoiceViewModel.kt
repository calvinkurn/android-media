package com.tokopedia.chat_common.view.viewmodel

abstract class InvoiceViewModel (
        val id: String,
        val invoiceCode: String,
        val productName: String,
        val date: String,
        val imageUrl: String,
        val invoiceUrl: String,
        val statusId: Int,
        val status: String,
        val totalPriceAmount: String
)