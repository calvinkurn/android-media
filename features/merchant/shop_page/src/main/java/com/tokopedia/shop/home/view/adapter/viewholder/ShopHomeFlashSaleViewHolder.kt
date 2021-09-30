package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel

class ShopHomeFlashSaleViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeFlashSaleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_flash_sale_widget
    }

    override fun bind(element: ShopHomeFlashSaleUiModel?) {
        TODO("Not yet implemented")
    }
}