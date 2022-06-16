package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoLoadingUiModel

class ShopInfoLoadingViewHolder(
    itemView: View
): AbstractViewHolder<ShopInfoLoadingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_seller_menu_shop_info_loading
    }

    override fun bind(element: ShopInfoLoadingUiModel?) {}
}