package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.ShopOrderUiModel
import kotlinx.android.synthetic.main.item_seller_menu_order_section.view.*

class ShopOrderViewHolder(itemView: View): AbstractViewHolder<ShopOrderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_order_section
    }

    override fun bind(order: ShopOrderUiModel) {
        val newOrderIcon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_seller_menu_new_order)
        val deliveryIcon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_seller_menu_delivery)

        itemView.imageNewOrder.setImageDrawable(newOrderIcon)
        itemView.imageDelivery.setImageDrawable(deliveryIcon)

        itemView.newOrderCount.text = "${order.newOrderCount}"
        itemView.deliveryCount.text = "${order.deliveryCount}"

        itemView.newOrderCount.visible()
        itemView.deliveryCount.visible()
    }
}