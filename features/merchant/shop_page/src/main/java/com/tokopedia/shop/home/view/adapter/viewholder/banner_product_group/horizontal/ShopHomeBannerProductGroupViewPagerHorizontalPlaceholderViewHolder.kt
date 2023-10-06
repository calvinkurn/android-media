package com.tokopedia.shop.home.view.adapter.viewholder.banner_product_group.horizontal

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel

class ShopHomeBannerProductGroupViewPagerHorizontalPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BannerProductGroupUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_banner_product_group_viewpager_horizontal_placeholder
    }

    override fun bind(model: BannerProductGroupUiModel) {

    }

}
