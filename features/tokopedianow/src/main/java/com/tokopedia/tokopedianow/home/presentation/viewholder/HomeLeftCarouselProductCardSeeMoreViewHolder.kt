package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselProductCardSeeMoreBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSeeMoreUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselProductCardSeeMoreViewHolder(view: View)
    : AbstractViewHolder<HomeLeftCarouselProductCardSeeMoreUiModel>(view){

    companion object{
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_product_card_see_more
    }

    private val binding: ItemTokopedianowHomeLeftCarouselProductCardSeeMoreBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselProductCardSeeMoreUiModel) {
        binding?.backgroundBannerMixMore?.setOnClickListener {
            // nothing to do
        }
        binding?.backgroundBannerMixMore?.loadImageWithoutPlaceholder(element.backgroundImage)
        binding?.containerBannerMixMore?.setOnClickListener {
            // nothing to do
        }
    }
}