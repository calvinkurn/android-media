package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupProductExpandBinding
import com.tokopedia.checkout.view.uimodel.ShipmentGroupProductExpandModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone

class ShipmentGroupProductExpandViewHolder(
    itemView: View,
    private val listener: Listener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_product_expand
    }

    private val binding: ItemShipmentGroupProductExpandBinding =
        ItemShipmentGroupProductExpandBinding.bind(itemView)

    fun bind(shipmentGroupProductExpand: ShipmentGroupProductExpandModel) {
        with(binding) {
            rlExpandOtherProduct.setOnClickListener {
                listener?.onClickExpandGroupProduct(
                    shipmentGroupProductExpand.copy(
                        shipmentCartItem = shipmentGroupProductExpand.shipmentCartItem.copy(
                            isStateAllItemViewExpanded = !shipmentGroupProductExpand.shipmentCartItem.isStateAllItemViewExpanded
                        )
                    )
                )
            }
            if (shipmentGroupProductExpand.shipmentCartItem.isStateAllItemViewExpanded) {
                vSeparatorMultipleProductSameStore.visibility = View.GONE
                tvExpandOtherProduct.setText(R.string.label_hide_other_item_new)
                ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                vSeparatorMultipleProductSameStore.gone()
                tvExpandOtherProduct.text = itemView.context.getString(
                    R.string.label_show_other_item_count,
                    shipmentGroupProductExpand.shipmentCartItem.cartItemModels.size
                )
                ivExpandOtherProduct.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
            }
        }
    }

    interface Listener {

        fun onClickExpandGroupProduct(shipmentGroupProductExpand: ShipmentGroupProductExpandModel)
    }
}
