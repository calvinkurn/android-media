package com.tokopedia.search.result.product.inspirationlistatc

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardModel
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionListAtcBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.utils.SEARCH_PAGE_RESULT_MAX_LINE
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup as LabelGroupReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.ShopBadge as ShopBadgeReimagine

class InspirationListAtcViewHolder(
    itemView: View,
    private val inspirationListAtcListener: InspirationListAtcListener,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val reimagineSearch2Component: Search2Component,
) : AbstractViewHolder<InspirationListAtcDataView>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_list_atc
    }
    private var binding: SearchInspirationCarouselOptionListAtcBinding? by viewBinding()

    private val isReimagine: Boolean
        get() = reimagineSearch2Component.isReimagineCarousel()

    override fun bind(item: InspirationListAtcDataView) {
        bindTitle(item)
        bindSubtitle(item)
        bindSeeMoreClick(item)
        bindHeader(item)
        bindProductCarousel(item)
        bindReimagineProductCarousel(item)
    }

    private fun bindTitle(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcTitle?.shouldShowWithAction(!isReimagine) {
            binding?.inspirationCarouselListAtcTitle?.text = item.option.title
        }
    }

    private fun bindSubtitle(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcSubtitle?.shouldShowWithAction(!isReimagine) {
            binding?.inspirationCarouselListAtcSubtitle?.text = item.option.subtitle
        }
    }

    private fun bindSeeMoreClick(item: InspirationListAtcDataView) {
        val seeMoreView = binding?.inspirationCarouselListAtcSeeMore
        val isShowSeeMoreView = !isReimagine && item.option.applink.isNotEmpty()

        seeMoreView?.shouldShowWithAction(isShowSeeMoreView) {
            seeMoreView.setOnClickListener {
                inspirationListAtcListener.onListAtcSeeMoreClicked(item.option)
            }
        }
    }

    private fun bindHeader(item: InspirationListAtcDataView) {
        val headerView = binding?.inspirationCarouselListAtcHeader ?: return

        headerView.shouldShowWithAction(isReimagine) {
            headerView.bind(
                channelHeader = ChannelHeader(
                    name = item.option.title,
                    subtitle = item.option.subtitle,
                    applink = item.option.applink,
                    iconSubtitleUrl = item.option.iconSubtitle,
                    headerType = ChannelHeader.HeaderType.REVAMP,
                ),
                listener = object : HomeChannelHeaderListener {
                    override fun onSeeAllClick(link: String) {
                        inspirationListAtcListener.onListAtcSeeMoreClicked(item.option)
                    }
                },
                maxLines = SEARCH_PAGE_RESULT_MAX_LINE,
            )
        }
    }

    private fun bindProductCarousel(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcProductCarousel?.shouldShowWithAction(!isReimagine) {
            val products = item.option.product

            binding?.inspirationCarouselListAtcProductCarousel?.bindCarouselProductCardViewGrid(
                recyclerViewPool = recycledViewPool,
                productCardModelList = products.map { it.toProductCardModel() },
                carouselProductCardOnItemClickListener = object :
                    CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return
                        inspirationListAtcListener.onListAtcItemClicked(product)
                    }
                },
                carouselProductCardOnItemImpressedListener = object :
                    CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(
                        productCardModel: ProductCardModel,
                        carouselProductCardPosition: Int
                    ) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return

                        inspirationListAtcListener.onListAtcItemImpressed(product)
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return if (carouselProductCardPosition < products.size)
                            products[carouselProductCardPosition]
                        else null
                    }
                },
                carouselProductCardOnItemAddToCartListener = object :
                    CarouselProductCardListener.OnItemAddToCartListener {
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

    private fun bindReimagineProductCarousel(item: InspirationListAtcDataView) {
        binding?.inspirationCarouselListAtcProductCarouselReimagine?.shouldShowWithAction(isReimagine) {
            binding?.inspirationCarouselListAtcProductCarouselReimagine?.bind(
                CarouselProductCardModel(
                    itemList = carouselProductCardReimagineItemList(item),
                    recycledViewPool = recycledViewPool,
                )
            )
        }
    }

    private fun carouselProductCardReimagineItemList(item: InspirationListAtcDataView) =
        item.option.product.map { product ->
            val shopBadge = product.badgeItemDataViewList.firstOrNull()
            CarouselProductCardGridModel(
                productCardModel = ProductCardModelReimagine(
                    imageUrl = product.imgUrl,
                    isAds = product.isOrganicAds,
                    name = product.name,
                    price = product.priceStr,
                    slashedPrice = product.originalPrice,
                    discountPercentage = product.discountPercentage,
                    rating = product.ratingAverage,
                    labelGroupList = product.labelGroupDataList.map { labelGroup ->
                        LabelGroupReimagine(
                            title = labelGroup.title,
                            position = labelGroup.position,
                            type = labelGroup.type,
                            imageUrl = labelGroup.imageUrl,
                        )
                    },
                    shopBadge = ShopBadgeReimagine(
                        imageUrl = shopBadge?.imageUrl ?: "",
                        title = shopBadge?.title ?: "",
                    ),
                    hasAddToCart = true,
                ),
                impressHolder = { product },
                onImpressed = { inspirationListAtcListener.onListAtcItemImpressed(product) },
                onClick = { inspirationListAtcListener.onListAtcItemClicked(product) },
                onAddToCart = { inspirationListAtcListener.onListAtcItemAddToCart(product, item.type) },
            )
        }
}
