package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import kotlinx.android.synthetic.main.item_seller_menu_product_section.view.*

class ShopProductViewHolder(
        itemView: View,
        private val tracker: SellerMenuTracker?
): AbstractViewHolder<ShopProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_product_section
    }

    override fun bind(product: ShopProductUiModel) {
        val productCountTxt = itemView.context.getString(R.string.seller_menu_product_count, product.count)
        val chevronRight = ContextCompat.getDrawable(itemView.context, R.drawable.ic_seller_menu_chevron_right)

        itemView.textProductCount.text = productCountTxt
        itemView.imageChevronRight.setImageDrawable(chevronRight)

        itemView.setOnClickListener {
            itemView.context?.let {
                if (product.isShopOwner) {
                    RouteManager.route(itemView.context, ApplinkConst.PRODUCT_MANAGE)
                } else {
                    RouteManager.route(it, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.MANAGE_PRODUCT))
                }
            }
            tracker?.sendEventClickProductList()
        }
    }
}