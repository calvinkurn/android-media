package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.carousel_see_more_card_item_grid_layout.view.*

internal class CarouselSeeMoreCardGridViewHolder(
    itemView: View
): BaseProductCardViewHolder<CarouselSeeMoreCardModel>(itemView, null) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_see_more_card_item_grid_layout
    }

    override fun bind(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) {
        setCarouselProductCardListeners(carouselSeeMoreCardModel)
    }

    private fun setCarouselProductCardListeners(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) {
        val onClickSeeMoreListener = carouselSeeMoreCardModel.getOnSeeMoreClickListener()

        itemView.card_see_more?.setOnClickListener {
            onClickSeeMoreListener?.onSeeMoreClick()
        }
    }

    override fun recycle() {
    }
}