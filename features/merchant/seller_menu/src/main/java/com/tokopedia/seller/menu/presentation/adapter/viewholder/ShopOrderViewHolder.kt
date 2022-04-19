package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.databinding.ItemSellerMenuOrderSectionBinding
import com.tokopedia.seller.menu.presentation.uimodel.ShopOrderUiModel

class ShopOrderViewHolder(
    itemView: View,
    private val sellerMenuTracker: SellerMenuTracker?
): AbstractViewHolder<ShopOrderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_order_section
    }

    private val context by lazy { itemView.context }

    private val binding = ItemSellerMenuOrderSectionBinding.bind(itemView)

    override fun bind(order: ShopOrderUiModel) {
        renderOrderCount(order)
        setClickListeners(order.isShopOwner)
    }

    private fun renderOrderCount(order: ShopOrderUiModel) {
        binding.newOrderCount.text = "${order.newOrderCount}"
        binding.readyToShipCount.text = "${order.readyToShip}"

        binding.newOrderCount.showWithCondition(order.newOrderCount > 0)
        binding.readyToShipCount.showWithCondition(order.readyToShip > 0)
    }

    private fun setClickListeners(isShopOwner: Boolean) {
        binding.cardNewOrder.setOnClickListener {
            if (isShopOwner) {
                RouteManager.route(context, ApplinkConst.SELLER_NEW_ORDER)
            } else {
                RouteManager.route(context, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.NEW_ORDER))
            }
            sellerMenuTracker?.sendEventClickOrderNew()
        }

        binding.cardReadyToShip.setOnClickListener {
            if (isShopOwner) {
                RouteManager.route(context, ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP)
            } else {
                RouteManager.route(context, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.READY_TO_SHIP_ORDER))
            }
            sellerMenuTracker?.sendEventClickOrderReadyToShip()
        }
    }
}