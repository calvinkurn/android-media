package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.showcase_navigation.ShopHomeShowcaseNavigationUiModel

class ShopHomeShowCaseNavigationTopMainBannerPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_top_main_banner_placeholder
    }

    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {

    }



}
