package com.tokopedia.search.result.product.samesessionrecommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultSameSessionRecommendationProductLayoutBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselListener
import com.tokopedia.utils.view.binding.viewBinding

class SameSessionRecommendationProductViewHolder(
    view: View,
    private val inspirationCarouselListener: InspirationCarouselListener,
) : AbstractViewHolder<InspirationCarouselDataView.Option.Product>(view) {
    private var binding: SearchResultSameSessionRecommendationProductLayoutBinding? by viewBinding()

    override fun bind(element: InspirationCarouselDataView.Option.Product) {
        binding?.productCardView?.let {
            it.setProductModel(element.toProductCardModel())
            it.applyCarousel()

            it.setOnClickListener {
                inspirationCarouselListener.onInspirationCarouselGridProductClicked(element)
            }

            it.setImageProductViewHintListener(element, createViewHintListener(element))
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
            freeOngkir = freeOngkirDataView.toFreeOngkir(),
            cardInteraction = true,
            customVideoURL = customVideoURL,
            pageSource = ProductCardModel.PageSource.SEARCH,
        )
    }

    private fun FreeOngkirDataView.toFreeOngkir() :ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }

    private fun List<LabelGroupDataView>?.toProductCardModelLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.imageUrl
            )
        } ?: listOf()
    }

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun createViewHintListener(product: InspirationCarouselDataView.Option.Product): ViewHintListener {
        return object : ViewHintListener {
            override fun onViewHint() {
                inspirationCarouselListener.onInspirationCarouselGridProductImpressed(product)
            }
        }
    }

    override fun onViewRecycled() {
        binding?.productCardView?.recycle()
    }

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_same_session_recommendation_product_layout
    }
}
