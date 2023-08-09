package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel

class ShopHomeV4TerlarisViewHolderTest(
    itemView: View
) : AbstractViewHolder<ShopHomeV4TerlarisUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_v4_terlaris_placeholder
    }

    override fun bind(element: ShopHomeV4TerlarisUiModel?) {
        val data = element
    }
}
