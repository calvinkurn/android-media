package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopHomeV4TerlarisPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_v4_terlaris_placeholder
    }

    override fun bind(element: BaseShopHomeWidgetUiModel?) { }
}
