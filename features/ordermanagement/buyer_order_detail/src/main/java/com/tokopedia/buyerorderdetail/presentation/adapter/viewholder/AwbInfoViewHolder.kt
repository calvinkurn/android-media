package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class AwbInfoViewHolder(itemView: View?) : BaseToasterViewHolder<ShipmentInfoUiModel.AwbInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_awb

        private const val LABEL_AWB = "awb"
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val icBuyerOrderDetailCopyAwb = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailCopyAwb)
    private val tvBuyerOrderDetailAwbValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailAwbValue)

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
        container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        bind(element)
        container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setupClickListener() {
        icBuyerOrderDetailCopyAwb?.setOnClickListener {
            copyAwb()
        }
    }

    private fun copyAwb() {
        element?.let {
            Utils.copyText(itemView.context, LABEL_AWB, it.awbNumber)
            showToaster(itemView.context.getString(R.string.message_awb_copied))
            BuyerOrderDetailTracker.eventClickCopyOrderAwb(it.orderStatusId, it.orderId)
        }
    }

    private fun setupAwbNumber(awbNumber: String) {
        tvBuyerOrderDetailAwbValue?.text = awbNumber
    }
}