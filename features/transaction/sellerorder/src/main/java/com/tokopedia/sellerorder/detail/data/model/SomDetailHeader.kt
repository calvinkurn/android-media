package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL

/**
 * Created by fwidjaja on 2019-10-03.
 */
data class SomDetailHeader(
    val statusCode: Int = -1,
    val statusText: String = "",
    val statusIndicatorColor: String = "",
    val isBuyerRequestCancel: Boolean = false,
    val invoice: String = "",
    val invoiceUrl: String = "",
    val paymentDate: String = "",
    val custName: String = "",
    val deadlineText: String = "",
    val deadlineColor: String = "",
    val deadlineStyle: Int = Int.ZERO,
    val listLabelOrder: List<SomDetailOrder.GetSomDetail.LabelInfo> = listOf(),
    val orderId: String = "",
    val awbUploadUrl: String = "",
    val awbUploadProofText: String = "",
    val onlineBookingCode: String = "",
    val onlineBookingState: Int = -1,
    val onlineBookingType: String = "",
    val fullFillBy: Int = 0,
    val isWarehouse: Boolean = false,
    val tickerInfo: TickerInfo = TickerInfo(),
    val pofStatus: Int = STATUS_INITIAL
)
