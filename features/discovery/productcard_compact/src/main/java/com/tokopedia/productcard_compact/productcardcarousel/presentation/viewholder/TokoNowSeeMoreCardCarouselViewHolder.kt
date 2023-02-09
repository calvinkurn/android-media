package com.tokopedia.productcard_compact.productcardcarousel.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard_compact.R
import com.tokopedia.productcard_compact.databinding.ItemTokopedianowSeeMoreCardCarouselBinding
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowSeeMoreCardCarouselViewHolder(
    view: View,
    private val listener: TokoNowCarouselProductCardSeeMoreListener? = null
) : AbstractViewHolder<TokoNowSeeMoreCardCarouselUiModel>(view){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_see_more_card_carousel
    }

    private val binding: ItemTokopedianowSeeMoreCardCarouselBinding? by viewBinding()

    override fun bind(element: TokoNowSeeMoreCardCarouselUiModel) {
        binding?.apply {
            backgroundBannerMixMore.setOnClickListener {
                listener?.onProductCardSeeMoreClickListener(
                    seeMoreUiModel = element
                )
            }
            containerBannerMixMore.setOnClickListener {
                listener?.onProductCardSeeMoreClickListener(
                    seeMoreUiModel = element
                )
            }
        }
    }

    interface TokoNowCarouselProductCardSeeMoreListener {
        fun onProductCardSeeMoreClickListener(
            seeMoreUiModel: TokoNowSeeMoreCardCarouselUiModel
        )
    }
}
