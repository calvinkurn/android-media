package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.util.loadImageWithoutPlaceholder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardSeeMoreBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardSeeMoreViewHolder(
    view: View,
    private val listener: HomeLeftCarouselAtcProductCardSeeMoreListener? = null
) : AbstractViewHolder<HomeLeftCarouselAtcProductCardSeeMoreUiModel>(view){

    companion object{
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card_see_more
    }

    private val binding: ItemTokopedianowHomeLeftCarouselAtcProductCardSeeMoreBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselAtcProductCardSeeMoreUiModel) {
        binding?.backgroundBannerMixMore?.setOnClickListener {
            listener?.onProductCardSeeMoreClickListener(element)
        }
        binding?.backgroundBannerMixMore?.loadImageWithoutPlaceholder(element.backgroundImage)
        binding?.containerBannerMixMore?.setOnClickListener {
            listener?.onProductCardSeeMoreClickListener(element)
        }
    }

    interface HomeLeftCarouselAtcProductCardSeeMoreListener {
        fun onProductCardSeeMoreClickListener(product: HomeLeftCarouselAtcProductCardSeeMoreUiModel)
    }
}