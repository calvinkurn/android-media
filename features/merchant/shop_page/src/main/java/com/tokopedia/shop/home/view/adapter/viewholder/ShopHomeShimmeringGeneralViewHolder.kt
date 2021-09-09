package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopHomeShimmeringGeneralViewHolder(
        itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_shimmering_general
    }

    override fun bind(model: BaseShopHomeWidgetUiModel) {

    }

}