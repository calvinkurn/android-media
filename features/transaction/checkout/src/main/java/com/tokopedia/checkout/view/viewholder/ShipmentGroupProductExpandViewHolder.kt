package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupProductExpandBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

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

    fun bind(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding) {
            rlExpandOtherProduct.setOnClickListener {
                shipmentCartItemModel.isStateAllItemViewExpanded =
                    !shipmentCartItemModel.isStateAllItemViewExpanded
                if (shipmentCartItemModel.isStateAllItemViewExpanded) {
                    listener?.onClickExpandGroupProduct(
                        bindingAdapterPosition,
                        shipmentCartItemModel
                    )
                } else {
                    listener?.onClickCollapseGroupProduct(
                        bindingAdapterPosition,
                        shipmentCartItemModel
                    )
                }
            }
            vSeparatorMultipleProductSameStore.visible()
            if (shipmentCartItemModel.isStateAllItemViewExpanded) {
                tvExpandOtherProduct.setText(R.string.label_hide_other_item_new)
                ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                val expandItemCount =
                    shipmentCartItemModel.cartItemModels.size - 1
                tvExpandOtherProduct.text = itemView.context.getString(
                    R.string.label_show_other_item_count,
                    expandItemCount
                )
                ivExpandOtherProduct.setImage(IconUnify.CHEVRON_DOWN, null, null, null, null)
            }
        }
    }

    interface Listener {

        fun onClickCollapseGroupProduct(
            position: Int,
            shipmentCartItemModel: ShipmentCartItemModel
        )

        fun onClickExpandGroupProduct(
            position: Int,
            shipmentCartItemModel: ShipmentCartItemModel
        )
    }
}
