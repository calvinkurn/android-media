package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_shipment_info_awb.view.*

class AwbInfoViewHolder(itemView: View?) : AbstractViewHolder<ShipmentInfoUiModel.AwbInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_awb
    }

    override fun bind(element: ShipmentInfoUiModel.AwbInfoUiModel?) {
        element?.let {
            setupAwbNumber(it.awbNumber)
        }
    }

    private fun setupAwbNumber(awbNumber: String) {
        itemView.tvBuyerOrderDetailAwbValue?.text = awbNumber
    }
}