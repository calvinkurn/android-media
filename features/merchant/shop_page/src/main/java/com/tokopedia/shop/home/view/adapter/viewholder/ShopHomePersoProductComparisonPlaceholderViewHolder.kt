package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

class ShopHomePersoProductComparisonPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_perso_product_comparison_placeholder
    }

    override fun bind(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel) {}
}
