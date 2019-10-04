package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-03.
 */
data class SomDetailHeader (
        val statusText: String = "",
        val invoice: String = "",
        val invoiceUrl: String = "",
        val paymentDate: String = "",
        val custName: String = "",
        val deadlineText: String = "",
        val deadlineColor: String = "",
        val listLabelOrder: List<SomDetailOrder.Data.GetSomDetail.LabelInfo> = listOf())