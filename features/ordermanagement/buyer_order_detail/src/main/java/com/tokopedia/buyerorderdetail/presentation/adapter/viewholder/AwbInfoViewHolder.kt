package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import kotlinx.android.synthetic.main.item_buyer_order_detail_shipment_info_awb.view.*

class AwbInfoViewHolder(itemView: View?) : BaseToasterViewHolder<ShipmentInfoUiModel.AwbInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_awb

        private const val LABEL_AWB = "awb"
    }

    private var element: ShipmentInfoUiModel.AwbInfoUiModel? = null

    init {
        setupClickListener()
    }

    override fun bind(element: ShipmentInfoUiModel.AwbInfoUiModel?) {
        element?.let {
            this.element = it
            setupAwbNumber(it.awbNumber)
        }
    }

    override fun bind(element: ShipmentInfoUiModel.AwbInfoUiModel?, payloads: MutableList<Any>) {
        itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        bind(element)
        itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setupClickListener() {
        itemView.icBuyerOrderDetailCopyAwb?.setOnClickListener {
            copyAwb()
        }
    }

    private fun copyAwb() {
        element?.let {
            Utils.copyText(itemView.context, LABEL_AWB, it.awbNumber)
            showToaster(itemView.context.getString(R.string.message_invoice_copied))
        }
    }

    private fun setupAwbNumber(awbNumber: String) {
        itemView.tvBuyerOrderDetailAwbValue?.text = awbNumber
    }
}