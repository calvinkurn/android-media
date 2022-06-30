package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardViewAllCardBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselViewAllCardViewHolder(
    itemView: View
): BaseProductCardViewHolder<CarouselViewAllCardModel>(itemView, null)  {
    private var binding: CarouselProductCardViewAllCardBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_view_all_card
    }

    override fun bind(model: CarouselViewAllCardModel) {
        bindTitle(model)
        bindClickListener(model)
    }

    private fun bindTitle(model: CarouselViewAllCardModel) {
        binding?.carouselProductViewAllCard?.title = model.data.title
    }

    private fun bindClickListener(model: CarouselViewAllCardModel) {
        val onViewAllCardClickListener = model.getOnViewAllCardClickListener()

        binding?.carouselProductViewAllCard?.setCta("") {
            onViewAllCardClickListener?.onViewAllCardClick()
        }
    }

    override fun recycle() {
    }
}