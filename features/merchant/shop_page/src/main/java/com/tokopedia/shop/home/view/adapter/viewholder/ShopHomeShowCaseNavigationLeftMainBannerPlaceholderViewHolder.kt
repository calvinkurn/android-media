package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationLeftMainBannerPlaceholderBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseNavigationLeftMainBannerPlaceholderViewHolder(
    itemView: View,
) : AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_left_main_banner_placeholder

    }

    private val viewBinding: ItemShopHomeShowcaseNavigationLeftMainBannerPlaceholderBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {

    }


}
