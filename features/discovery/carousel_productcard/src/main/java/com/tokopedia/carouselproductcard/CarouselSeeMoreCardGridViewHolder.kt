package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.carouselproductcard.databinding.CarouselSeeMoreCardItemGridLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselSeeMoreCardGridViewHolder(
    itemView: View
): BaseProductCardViewHolder<CarouselSeeMoreCardModel>(itemView, null) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_see_more_card_item_grid_layout
    }

    private var binding: CarouselSeeMoreCardItemGridLayoutBinding? by viewBinding()

    override fun bind(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) {
        setCarouselProductCardListeners(carouselSeeMoreCardModel)
    }

    private fun setCarouselProductCardListeners(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) {
        val onClickSeeMoreListener = carouselSeeMoreCardModel.getOnSeeMoreClickListener()

        binding?.cardSeeMore?.setOnClickListener {
            onClickSeeMoreListener?.onSeeMoreClick()
        }
    }

    override fun recycle() {
    }
}