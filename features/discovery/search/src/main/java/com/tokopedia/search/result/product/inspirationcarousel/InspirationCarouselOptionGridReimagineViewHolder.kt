package com.tokopedia.search.result.product.inspirationcarousel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionGridReimagineBinding
import com.tokopedia.utils.view.binding.viewBinding

class InspirationCarouselOptionGridReimagineViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationCarouselListener
): AbstractViewHolder<InspirationCarouselDataView.Option.Product>(itemView) {

    private val binding: SearchInspirationCarouselOptionGridReimagineBinding? by viewBinding()

    override fun bind(product: InspirationCarouselDataView.Option.Product) {
        binding?.inspirationCarouselProductCard?.run {
            setProductModel(productCardModel(product))

            setOnClickListener {
                inspirationCarouselListener.onInspirationCarouselGridProductClicked(product)
            }

            addOnImpressionListener(product) {
                inspirationCarouselListener.onInspirationCarouselGridProductImpressed(product)
            }
        }
    }

    private fun productCardModel(
        product: InspirationCarouselDataView.Option.Product,
    ): ProductCardModel {
        val shopBadge = product.badgeItemDataViewList.firstOrNull()

        return ProductCardModel(
            imageUrl = product.imgUrl,
            isAds = product.isOrganicAds,
            name = product.name,
            price = product.priceStr,
            rating = product.ratingAverage,
            slashedPrice = product.originalPrice,
            discountPercentage = product.discountPercentage,
            labelGroupList = product.labelGroupDataList.map { labelGroup ->
                ProductCardModel.LabelGroup(
                    title = labelGroup.title,
                    position = labelGroup.position,
                    type = labelGroup.type,
                    imageUrl = labelGroup.imageUrl,
                )
            },
            shopBadge = ProductCardModel.ShopBadge(
                title = shopBadge?.title ?: "",
                imageUrl = shopBadge?.imageUrl ?: "",
            ),
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_inspiration_carousel_option_grid_reimagine
    }
}
