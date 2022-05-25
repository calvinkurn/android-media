package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardSeeMoreBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSeeMoreUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardSeeMoreViewHolder(
    view: View,
    private val listener: HomeLeftCarouselProductCardSeeMoreListener? = null
) : AbstractViewHolder<HomeLeftCarouselProductCardSeeMoreUiModel>(view){

    companion object{
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card_see_more
    }

    private val binding: ItemTokopedianowHomeLeftCarouselAtcProductCardSeeMoreBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselProductCardSeeMoreUiModel) {
        binding?.backgroundBannerMixMore?.setOnClickListener {
            listener?.onProductCardSeeMoreClickListener(element)
        }
        binding?.backgroundBannerMixMore?.loadImageWithoutPlaceholder(element.backgroundImage)
        binding?.containerBannerMixMore?.setOnClickListener {
            listener?.onProductCardSeeMoreClickListener(element)
        }
    }

    interface HomeLeftCarouselProductCardSeeMoreListener {
        fun onProductCardSeeMoreClickListener(product: HomeLeftCarouselProductCardSeeMoreUiModel)
    }
}