package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.ShopBusinessUiModel

class ShopBusinessViewHolder(itemView: View): AbstractViewHolder<ShopBusinessUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_business_section
    }

    override fun bind(product: ShopBusinessUiModel) {}
}