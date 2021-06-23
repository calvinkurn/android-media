package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifyprinciples.Typography

class DropShipperInfoViewHolder(
        itemView: View?
) : AbstractViewHolder<ShipmentInfoUiModel.DropShipperInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_shipment_info_dropship
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val tvDropShipperName = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailDropShipperNameValue)
    private val tvDropShipperPhoneNumber = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailDropShipperPhoneNumberValue)

    override fun bind(element: ShipmentInfoUiModel.DropShipperInfoUiModel?) {
        setupDropShipperName(element?.name)
        setupDropShipperPhoneNumber(element?.phoneNumber)
    }

    override fun bind(element: ShipmentInfoUiModel.DropShipperInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ShipmentInfoUiModel.DropShipperInfoUiModel && newItem is ShipmentInfoUiModel.DropShipperInfoUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    if (oldItem.name != newItem.name) {
                        setupDropShipperName(newItem.name)
                    }
                    if (oldItem.phoneNumber != newItem.phoneNumber) {
                        setupDropShipperPhoneNumber(newItem.phoneNumber)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupDropShipperName(name: String?) {
        tvDropShipperName?.text = name.orEmpty()
        tvDropShipperName?.showWithCondition(!name.isNullOrBlank())
    }

    private fun setupDropShipperPhoneNumber(phoneNumber: String?) {
        tvDropShipperPhoneNumber?.text = phoneNumber.orEmpty()
        tvDropShipperPhoneNumber?.showWithCondition(!phoneNumber.isNullOrBlank())
    }
}