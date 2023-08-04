package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeShowcaseCarouselBannerBinding
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeShowCaseCarouselViewHolder(itemView: View) : AbstractViewHolder<ShopHomeShowcaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_showcase_left_main_banner
        private const val SHOW_VIEW_ALL_SHOWCASE_THRESHOLD = 5
    }

    private val viewBinding: ItemShopHomeShowcaseCarouselBannerBinding? by viewBinding()


    override fun bind(model: ShopHomeShowcaseUiModel) {
        viewBinding?.tpgTitle?.text = model.categoryHeader.title
        viewBinding?.iconChevron?.isVisible = model.tabs.size > SHOW_VIEW_ALL_SHOWCASE_THRESHOLD
    }

}
