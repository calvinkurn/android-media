package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.BroadMatchViewModel
import com.tokopedia.search.result.presentation.model.FreeOngkirViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import kotlinx.android.synthetic.main.search_result_product_broad_match_layout.view.*

class BroadMatchViewHolder(
        itemView: View,
        private val broadMatchListener: BroadMatchListener
) : AbstractViewHolder<BroadMatchViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_broad_match_layout
    }

    override fun bind(element: BroadMatchViewModel) {
        bindTitle(element)
        bindSeeMore(element)
        setupRecyclerView(element)
    }

    private fun bindTitle(broadMatchViewModel: BroadMatchViewModel) {
        itemView.searchBroadMatchTitle?.text = broadMatchViewModel.keyword
    }

    private fun bindSeeMore(broadMatchViewModel: BroadMatchViewModel) {
        itemView.searchBroadMatchSeeMore?.showWithCondition(broadMatchViewModel.applink.isNotEmpty())

        itemView.searchBroadMatchSeeMore?.setOnClickListener {
            broadMatchListener.onBroadMatchSeeMoreClicked(broadMatchViewModel)
        }
    }

    private fun setupRecyclerView(dataModel: BroadMatchViewModel){
        val products = dataModel.broadMatchItemViewModelList
        itemView.searchBroadMatchList?.bindCarouselProductCardViewGrid(
                productCardModelList = products.map {
                    ProductCardModel(
                            productName = it.name,
                            formattedPrice = it.priceString,
                            productImageUrl = it.imageUrl,
                            countSoldRating = it.ratingAverage,
                            labelGroupList = it.labelGroupList.toProductCardLabelGroup(),
                            shopLocation = it.shopLocation,
                            shopBadgeList = it.badgeItemViewModelList.toProductCardModelShopBadges(),
                            freeOngkir = it.freeOngkirViewModel.toProductCardModelFreeOngkir(),
                            isTopAds = it.isOrganicAds,
                            hasThreeDots = true
                    )
                },
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return
                        broadMatchListener.onBroadMatchItemClicked(product)
                    }
                },
                carouselProductCardOnItemThreeDotsClickListener = object: CarouselProductCardListener.OnItemThreeDotsClickListener {
                    override fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return
                        broadMatchListener.onBroadMatchThreeDotsClicked(product)
                    }
                },
                carouselProductCardOnItemImpressedListener = object: CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val product = products.getOrNull(carouselProductCardPosition) ?: return
                        broadMatchListener.onBroadMatchItemImpressed(product)
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return products.getOrNull(carouselProductCardPosition)
                    }
                }
        )
    }

    private fun List<BadgeItemViewModel>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupViewModel>?.toProductCardLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(
                    title = it.title,
                    position = it.position,
                    type = it.type,
                    imageUrl = it.imageUrl
            )
        } ?: listOf()
    }

    private fun FreeOngkirViewModel.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }
}