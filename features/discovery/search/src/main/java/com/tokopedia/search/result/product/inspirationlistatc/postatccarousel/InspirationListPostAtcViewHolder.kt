package com.tokopedia.search.result.product.inspirationlistatc.postatccarousel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.reimagine.CarouselProductCardModel
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.discovery.common.reimagine.Search2Component
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationCarouselOptionListPostAtcBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup as LabelGroupReimagine
import com.tokopedia.productcard.reimagine.ProductCardModel.ShopBadge as ShopBadgeReimagine

class InspirationListPostAtcViewHolder(
    itemView: View,
    private val inspirationListAtcListener: InspirationListPostAtcListener,
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val reimagineSearch2Component: Search2Component,
    private val isShowSeparator: Boolean,
) : AbstractViewHolder<InspirationListPostAtcDataView>(itemView) {
    companion object {
        val LAYOUT = R.layout.search_inspiration_carousel_option_list_post_atc
    }
    private var binding: SearchInspirationCarouselOptionListPostAtcBinding? by viewBinding()

    private val isReimagine: Boolean
        get() = reimagineSearch2Component.isReimagineCarousel()

    override fun bind(item: InspirationListPostAtcDataView) {
        bindHeader(item)
        showSeparator()
        bindProductCarousel(item)
        bindReimagineProductCarousel(item)
        setCloseButtonListener(item)
        setCancelHideListListener(item)
        listPostAtcVisibility(item.isVisible)
    }

    private fun bindHeader(item: InspirationListPostAtcDataView) {
        val imageProductAtc = binding?.inspirationCarouselListPostAtcProductAtc ?: return
        val title = binding?.inspirationCarouselListPostAtcHeaderTitle ?: return
        val badge = binding?.inspirationCarouselListPostAtcHeaderBadge ?: return
        val subtitle = binding?.inspirationCarouselListPostAtcHeaderSubtitle ?: return

        title.text = item.option.title
        subtitle.text = item.option.subtitle
        imageProductAtc.loadImage(item.productAtc.imgUrl)
        badge.loadImage(item.option.iconSubtitle)
    }

    private fun setCloseButtonListener(item: InspirationListPostAtcDataView) {
        val closeButton = binding?.inspirationCarouselListPostAtcHeaderClose ?: return
        closeButton.setOnClickListener {
            inspirationListAtcListener.closeListPostAtcView(item)
        }
    }

    private fun setCancelHideListListener(item: InspirationListPostAtcDataView) {
        val cancelHide = binding?.inspirationCarouselListPostAtcCancelHide ?: return
        cancelHide.setOnClickListener {
            inspirationListAtcListener.cancelCloseListPostAtcView(item)
        }

    }

    private fun listPostAtcVisibility(hideList: Boolean) {
        setListPostAtcVisibility(hideList)
        setSuccessHideVisibility(hideList)
    }

    private fun setListPostAtcVisibility(showList : Boolean) {
        val listGroup = binding?.inspirationCarouselListPostAtcListGroup ?: return
        listGroup.showWithCondition(showList)
    }

    private fun setSuccessHideVisibility(showList : Boolean) {
        val hideMessageGroup = binding?.inspirationCarouselListPostAtcHideGroup ?: return
        hideMessageGroup.showWithCondition(!showList)
    }

    private fun showSeparator() {
        binding?.inspirationCarouselListPostAtcProductSeparator?.showWithCondition(isShowSeparator)
    }

    private fun bindProductCarousel(item: InspirationListPostAtcDataView) {
        binding?.inspirationCarouselListPostAtcProduct?.shouldShowWithAction(!isReimagine) {
            val products = item.option.product

            binding?.inspirationCarouselListPostAtcProduct?.bindCarouselProductCardViewGrid(
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

    private fun bindReimagineProductCarousel(item: InspirationListPostAtcDataView) {
        binding?.inspirationCarouselListPostAtcProductReimagine?.shouldShowWithAction(isReimagine) {
            binding?.inspirationCarouselListPostAtcProductReimagine?.bind(
                CarouselProductCardModel(
                    itemList = carouselProductCardReimagineItemList(item),
                    recycledViewPool = recycledViewPool,
                )
            )
        }
    }

    private fun carouselProductCardReimagineItemList(item: InspirationListPostAtcDataView) =
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
                        imageUrl = shopBadge?.imageUrl.orEmpty(),
                        title = shopBadge?.title.orEmpty(),
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
