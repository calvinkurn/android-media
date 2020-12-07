package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import kotlinx.android.synthetic.main.search_inspiration_carousel_option_grid.view.*

class InspirationCarouselOptionGridViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselViewModel.Option.Product>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid
    }

    override fun bind(item: InspirationCarouselViewModel.Option.Product) {

        itemView.optionGridCardView?.setProductModel(item.toProductCardModel())
        itemView.optionGridCardView?.applyCarousel()

        itemView.optionGridCardView?.setOnClickListener {
            inspirationCarouselListener.onInspirationCarouselGridProductClicked(item)
        }

        itemView.optionGridCardView?.setImageProductViewHintListener(item, createViewHintListener(item))
    }

    private fun InspirationCarouselViewModel.Option.Product.toProductCardModel(): ProductCardModel {
        val countSoldRating = labelGroupList.getOrNull(0)?.title ?: ""

        return ProductCardModel(
                productImageUrl = imgUrl,
                productName = name,
                formattedPrice = priceStr,
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                countSoldRating = countSoldRating
        )
    }

    private fun createViewHintListener(product: InspirationCarouselViewModel.Option.Product): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onImpressedInspirationCarouselGridProduct(product)
            }
        }
    }
}