package com.tokopedia.digital_checkout.data

data class PaymentSummary (
        val summaries: MutableList<Payment>
) {
    fun removeFromSummary(title: String) {
        val item: Payment = summaries.firstOrNull { it.title == title } ?: return
        val deletePosition = summaries.indexOf(item)
        summaries.removeAt(deletePosition)
    }

    fun addToSummary(payment: Payment) {
        summaries.add(payment)
    }

    fun addToSummary(position: Int, payment: Payment) {
        if (summaries.size >= position) {
            summaries.add(position, payment)
        } else summaries.add(payment)
    }

    fun changeSummaryValue(title: String, priceAmount: String) {
        val item: Payment = summaries.firstOrNull { it.title == title } ?: return
        item.priceAmount = priceAmount
    }

    data class Payment(
            val title: String,
            var priceAmount: String
    )
}
