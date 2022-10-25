package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
        binding?.apply {
            backgroundBannerMixMore.setOnClickListener {
                listener?.onProductCardSeeMoreClickListener(
                    product = element
                )
            }
            containerBannerMixMore.setOnClickListener {
                listener?.onProductCardSeeMoreClickListener(
                    product = element
                )
            }
        }
    }

    interface HomeLeftCarouselAtcProductCardSeeMoreListener {
        fun onProductCardSeeMoreClickListener(
            product: HomeLeftCarouselAtcProductCardSeeMoreUiModel
        )
    }
}
