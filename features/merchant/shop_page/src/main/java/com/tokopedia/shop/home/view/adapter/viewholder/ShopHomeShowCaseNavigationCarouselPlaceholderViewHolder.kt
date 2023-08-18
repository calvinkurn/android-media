package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationCarouselBannerBinding
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseNavigationCarouselBannerPlaceholderBinding
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseNavigationListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseNavigationUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseNavigationCarouselPlaceholderViewHolder(itemView: View) :
    AbstractViewHolder<ShopHomeShowcaseNavigationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_navigation_carousel_banner_placeholder
    }

    private val viewBinding: ItemShopHomeShowcaseNavigationCarouselBannerPlaceholderBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseNavigationUiModel) {

    }


}
