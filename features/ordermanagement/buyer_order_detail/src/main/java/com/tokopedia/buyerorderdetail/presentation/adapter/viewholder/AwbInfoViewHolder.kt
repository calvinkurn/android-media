package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel

class AwbInfoViewHolder(itemView: View?) : CopyableKeyValueViewHolder<ShipmentInfoUiModel.AwbInfoUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_awb
    }

    override fun copyText() {
        super.copyText()
        sendCopyAwbTrackerEvent()
    }

    private fun sendCopyAwbTrackerEvent() {
        element?.let {
            BuyerOrderDetailTracker.eventClickCopyOrderAwb(it.orderStatusId, it.orderId)
        }
    }
}