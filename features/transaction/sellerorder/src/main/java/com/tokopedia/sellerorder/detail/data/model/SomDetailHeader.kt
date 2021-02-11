package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.sellerorder.common.domain.model.TickerInfo

/**
 * Created by fwidjaja on 2019-10-03.
 */
data class SomDetailHeader (
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
        val listLabelOrder: List<SomDetailOrder.Data.GetSomDetail.LabelInfo> = listOf(),
        val orderId: String = "",
        val awbUploadUrl: String = "",
        val awbUploadProofText: String = "",
        val onlineBookingCode: String = "",
        val onlineBookingState: Int = -1,
        val onlineBookingType: String = "",
        val fullFillBy: Int = 0,
        val isWarehouse: Boolean = false,
        val tickerInfo: TickerInfo = TickerInfo())