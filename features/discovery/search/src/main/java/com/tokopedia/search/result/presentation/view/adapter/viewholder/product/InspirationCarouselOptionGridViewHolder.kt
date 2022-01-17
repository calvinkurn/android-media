package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionGridBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.listener.InspirationCarouselListener
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCarouselOptionGridViewHolder(
        itemView: View,
        private val inspirationCarouselListener: InspirationCarouselListener
) : AbstractViewHolder<InspirationCarouselDataView.Option.Product>(itemView) {

    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid
    }
    private var binding: SearchInspirationCarouselOptionGridBinding? by viewBinding()

    override fun bind(item: InspirationCarouselDataView.Option.Product) {
        binding?.optionGridCardView?.let {
            it.setProductModel(item.toProductCardModel())
            it.applyCarousel()

            it.setOnClickListener {
                inspirationCarouselListener.onInspirationCarouselGridProductClicked(item)
            }

            it.setImageProductViewHintListener(item, createViewHintListener(item))
        }
    }

    private fun InspirationCarouselDataView.Option.Product.toProductCardModel(): ProductCardModel {
        return ProductCardModel(
                productImageUrl = imgUrl,
                productName = name,
                formattedPrice = priceStr,
                slashedPrice = if (discountPercentage > 0) originalPrice else "",
                discountPercentage = if (discountPercentage > 0) "$discountPercentage%" else "",
                countSoldRating = ratingAverage,
                labelGroupList = labelGroupDataList.toProductCardModelLabelGroup(),
                shopLocation = shopLocation,
                shopBadgeList = badgeItemDataViewList.toProductCardModelShopBadges(),
        )
    }

    private fun List<LabelGroupDataView>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(position = it.position, title = it.title, type = it.type, imageUrl = it.imageUrl)
        } ?: listOf()
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun createViewHintListener(product: InspirationCarouselDataView.Option.Product): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onInspirationCarouselGridProductImpressed(product)
            }
        }
    }

    override fun onViewRecycled() {
        binding?.optionGridCardView?.recycle()
    }
}