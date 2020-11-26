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
) : AbstractViewHolder<InspirationCarouselViewModel.Option>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid
    }

    override fun bind(item: InspirationCarouselViewModel.Option) {
        val productOption = item.product.getOrNull(0) ?: return

        itemView.optionGridCardView?.setProductModel(productOption.toProductCardModel())

        itemView.optionGridCardView?.setOnClickListener {
            inspirationCarouselListener.onInspirationCarouselGridProductClicked(productOption)
        }

        itemView.optionGridCardView?.setImageProductViewHintListener(productOption, createViewHintListener(productOption))
    }

    private fun InspirationCarouselViewModel.Option.Product.toProductCardModel(): ProductCardModel {
        return ProductCardModel(
                productImageUrl = imgUrl,
                productName = name,
                formattedPrice = priceStr,
                ratingCount = rating,
                reviewCount = countReview,
                slashedPrice = originalPrice.toString(),
                discountPercentage = discountPercentage.toString()
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