package com.tokopedia.search.result.product.inspirationlistatc

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionListAtcBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.utils.view.binding.viewBinding

class InspirationListAtcViewHolder(
    itemView: View,
    private val inspirationListAtcListener: InspirationListAtcListener,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
) : AbstractViewHolder<InspirationListAtcDataView>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_list_atc
    }
    private var binding: SearchInspirationCarouselOptionListAtcBinding? by viewBinding()

    override fun bind(item: InspirationListAtcDataView) {
        bindTitle(item)
        bindSubtitle(item)
        bindSeeMoreClick(item)
        bindProductCarousel(item)
    }

    private fun bindTitle(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcTitle?.text = item.option.title
    }

    private fun bindSubtitle(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcSubtitle?.text = item.option.subtitle
    }

    private fun bindSeeMoreClick(item: InspirationListAtcDataView) {
        val seeMoreView = binding?.inspirationCarouselListAtcSeeMore

        seeMoreView?.shouldShowWithAction(item.option.applink.isNotEmpty()) {
            seeMoreView.setOnClickListener {
                inspirationListAtcListener.onListAtcSeeMoreClicked(item.option)
            }
        }
    }

    private fun bindProductCarousel(item: InspirationListAtcDataView) {
        val products = item.option.product

        binding?.inspirationCarouselListAtcProductCarousel?.bindCarouselProductCardViewGrid(
            recyclerViewPool = recycledViewPool,
            productCardModelList = products.map { it.toProductCardModel() },
            carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val product = products.getOrNull(carouselProductCardPosition) ?: return
                    inspirationListAtcListener.onListAtcItemClicked(product)
                }
            },
            carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                    val product = products.getOrNull(carouselProductCardPosition) ?: return

                    inspirationListAtcListener.onListAtcItemImpressed(product)
                }

                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return if (carouselProductCardPosition < products.size)
                        products[carouselProductCardPosition]
                    else null
                }
            },
            carouselProductCardOnItemAddToCartListener = object: CarouselProductCardListener.OnItemAddToCartListener {
                override fun onItemAddToCart(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int
                ) {
                    val product = products.getOrNull(carouselProductCardPosition) ?: return
                    inspirationListAtcListener.onListAtcItemAddToCart(product, item.type)
                }
            }
        )
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
            shopLocation = shopLocation.ifEmpty { shopName },
            shopBadgeList = badgeItemDataViewList.toProductCardModelShopBadges(),
            cardInteraction = true,
            hasAddToCartButton = true,
            isTopAds = isOrganicAds,
            pageSource = ProductCardModel.PageSource.SEARCH,
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
}
