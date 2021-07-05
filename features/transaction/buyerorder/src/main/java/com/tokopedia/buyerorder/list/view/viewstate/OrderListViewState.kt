package com.tokopedia.buyerorder.list.view.viewstate

import com.tokopedia.buyerorder.list.data.ActionButton
import com.tokopedia.buyerorder.list.data.MetaData

sealed class OrderListViewState
data class DotMenuVisibility(val visibility: Int) : OrderListViewState()
data class SetCategoryAndTitle(val categoryName: String, val title: String): OrderListViewState()
data class SetItemCount(val itemCount: Int): OrderListViewState()
data class SetTotal(val totalLabel: String, val totalValue: String, val textColor: String): OrderListViewState()
data class SetDate(val date: String?): OrderListViewState()
data class SetInvoice(val invoice: String): OrderListViewState()
data class SetConditionalInfo(val successCondInfoVisibility: Int, val successConditionalText: String?, val color: com.tokopedia.buyerorder.detail.data.Color?): OrderListViewState()
data class SetConditionalInfoBottom(val successCondInfoVisibility: Int, val successConditionalText: String?, val color: com.tokopedia.buyerorder.detail.data.Color?): OrderListViewState()
data class SetFailStatusBgColor(val statusColor: String): OrderListViewState()
data class SetStatus(val statusText: String): OrderListViewState()
data class SetMetaDataToCustomView(val metaData: MetaData): OrderListViewState()
data class SetPaymentAvatar(val imgUrl: String): OrderListViewState()
