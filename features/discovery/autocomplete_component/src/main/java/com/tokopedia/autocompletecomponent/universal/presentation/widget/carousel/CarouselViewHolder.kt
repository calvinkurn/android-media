package com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchCarouselItemLayoutBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.utils.view.binding.viewBinding

class CarouselViewHolder(
    itemView: View,
    private val carouselListener: CarouselListener
): AbstractViewHolder<CarouselDataView>(itemView) {
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.universal_search_carousel_item_layout
    }

    private var binding: UniversalSearchCarouselItemLayoutBinding? by viewBinding()

    override fun bind(data: CarouselDataView) {
        bindTitle(data)
        bindSubtitle(data)
        bindCarousel(data)
    }

    private fun bindTitle(data: CarouselDataView) {
        binding?.universalSearchCarouselTitle?.shouldShowWithAction(data.title.isNotEmpty()) {
            binding?.universalSearchCarouselTitle?.text = data.title
        }
    }

    private fun bindSubtitle(data: CarouselDataView) {
        binding?.universalSearchCarouselSubtitle?.shouldShowWithAction(data.subtitle.isNotEmpty()) {
            binding?.universalSearchCarouselSubtitle?.text = data.subtitle
        }
    }

    private fun bindCarousel(data: CarouselDataView) {
        val products = data.product

        binding?.universalSearchCarousel?.bindCarouselProductCardViewGrid(
            recyclerViewPool = carouselListener.carouselRecycledViewPool,
            productCardModelList = products.map {
                ProductCardModel(
                    productName = it.title,
                    formattedPrice = it.price,
                    discountPercentage = it.discountPercentage,
                    slashedPrice = it.originalPrice,
                    productImageUrl = it.imageUrl,
                    ratingString = it.ratingAverage,
                    countSoldRating = it.countSold,
                    shopLocation = it.shop.city,
                    shopName = it.shop.name,
                    shopBadgeList = it.badge.map { badge ->
                        ProductCardModel.ShopBadge(
                            isShown = badge.show,
                            imageUrl = badge.imageUrl
                        )
                    }
                )
            }
        )
    }
}