package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.ShopOrderUiModel
import kotlinx.android.synthetic.main.item_seller_menu_order_section.view.*

class ShopOrderViewHolder(
    itemView: View,
    private val sellerMenuTracker: SellerMenuTracker?
): AbstractViewHolder<ShopOrderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_order_section
    }

    private val context by lazy { itemView.context }

    override fun bind(order: ShopOrderUiModel) {
        renderOrderImage()
        renderOrderCount(order)
        setClickListeners(order.isShopOwner)
    }

    private fun renderOrderImage() {
        val newOrderIcon = ContextCompat.getDrawable(context, R.drawable.ic_seller_menu_new_order)
        val deliveryIcon = ContextCompat.getDrawable(context, R.drawable.ic_seller_menu_delivery)

        itemView.imageNewOrder.setImageDrawable(newOrderIcon)
        itemView.imageReadyToShip.setImageDrawable(deliveryIcon)
    }

    private fun renderOrderCount(order: ShopOrderUiModel) {
        itemView.newOrderCount.text = "${order.newOrderCount}"
        itemView.readyToShipCount.text = "${order.readyToShip}"

        itemView.newOrderCount.showWithCondition(order.newOrderCount > 0)
        itemView.readyToShipCount.showWithCondition(order.readyToShip > 0)
    }

    private fun setClickListeners(isShopOwner: Boolean) {
        itemView.cardNewOrder.setOnClickListener {
            if (isShopOwner) {
                RouteManager.route(context, ApplinkConst.SELLER_NEW_ORDER)
            } else {
                RouteManager.route(context, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.NEW_ORDER))
            }
            sellerMenuTracker?.sendEventClickOrderNew()
        }

        itemView.cardReadyToShip.setOnClickListener {
            if (isShopOwner) {
                RouteManager.route(context, ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP)
            } else {
                RouteManager.route(context, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.READY_TO_SHIP_ORDER))
            }
            sellerMenuTracker?.sendEventClickOrderReadyToShip()
        }
    }
}