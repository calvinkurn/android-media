package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.databinding.ItemSellerMenuProductSectionBinding
import com.tokopedia.seller.menu.presentation.uimodel.ShopProductUiModel

class ShopProductViewHolder(
        itemView: View,
        private val tracker: SellerMenuTracker?
): AbstractViewHolder<ShopProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_product_section
    }

    private val binding = ItemSellerMenuProductSectionBinding.bind(itemView)

    override fun bind(product: ShopProductUiModel) {
        val productCountTxt = itemView.context.getString(R.string.seller_menu_product_count, product.count)

        binding.textProductCount.text = productCountTxt

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