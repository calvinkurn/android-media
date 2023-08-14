package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupProductExpandBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.CartItemExpandModel

class ShipmentCartItemExpandViewHolder(
    itemView: View,
    private val listener: Listener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_product_expand
    }

    private val binding: ItemShipmentGroupProductExpandBinding =
        ItemShipmentGroupProductExpandBinding.bind(itemView)

    fun bind(cartItemExpandModel: CartItemExpandModel) {
        with(binding) {
            rlExpandOtherProduct.setOnClickListener {
                val position = bindingAdapterPosition
                val updatedCartExpandModel = cartItemExpandModel.copy(
                    isExpanded = !cartItemExpandModel.isExpanded
                )
                if (updatedCartExpandModel.isExpanded) {
                    listener?.onClickExpandGroupProduct(position, updatedCartExpandModel)
                } else {
                    listener?.onClickCollapseGroupProduct(position, updatedCartExpandModel)
                }
            }
            vSeparatorMultipleProductSameStore.visible()
            if (cartItemExpandModel.isExpanded) {
                tvExpandOtherProduct.setText(R.string.label_hide_other_item_new)
                ivExpandOtherProduct.setImage(IconUnify.CHEVRON_UP, null, null, null, null)
            } else {
                val expandItemCount =
                    cartItemExpandModel.cartSize - 1
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
            cartItemExpandModel: CartItemExpandModel
        )

        fun onClickExpandGroupProduct(
            position: Int,
            cartItemExpandModel: CartItemExpandModel
        )
    }
}
