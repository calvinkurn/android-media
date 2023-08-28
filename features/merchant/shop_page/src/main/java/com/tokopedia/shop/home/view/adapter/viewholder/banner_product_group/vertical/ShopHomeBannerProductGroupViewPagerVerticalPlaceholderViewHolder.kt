package com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.vertical

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel

class ShopHomeBannerProductGroupViewPagerVerticalPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopWidgetComponentBannerProductGroupUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_banner_product_group_viewpager_vertical_placeholder
    }

    override fun bind(model: ShopWidgetComponentBannerProductGroupUiModel) {

    }

}
