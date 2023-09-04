package com.tokopedia.carouselproductcard.reimagine.viewallcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardReimagineViewAllCardBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselProductCardViewAllCardViewHolder(
    itemView: View,
): AbstractViewHolder<CarouselProductCardViewAllCardModel>(itemView) {

    private val binding: CarouselProductCardReimagineViewAllCardBinding? by viewBinding()

    override fun bind(element: CarouselProductCardViewAllCardModel) {
        binding?.carouselProductCardReimagineViewAllCard?.run {
            isTitleNumberStyle = element.titleIsInteger
            title = element.title
            description = element.description
            mode = element.viewAllCardMode
            setCta(element.ctaText) {
                element.onClick()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_reimagine_view_all_card
    }
}
