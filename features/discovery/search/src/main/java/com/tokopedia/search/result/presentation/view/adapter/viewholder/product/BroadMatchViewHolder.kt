package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductBroadMatchLayoutBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.presentation.view.listener.BroadMatchListener
import com.tokopedia.utils.view.binding.viewBinding

class BroadMatchViewHolder(
        itemView: View,
        private val broadMatchListener: BroadMatchListener
) : AbstractViewHolder<BroadMatchDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_broad_match_layout
    }
    
    private var binding: SearchResultProductBroadMatchLayoutBinding? by viewBinding()

    override fun bind(element: BroadMatchDataView) {
        bindTitle(element)
        bindSeeMore(element)
        setupRecyclerView(element)
    }

    private fun bindTitle(broadMatchDataView: BroadMatchDataView) {
        val searchBroadMatchTitle = binding?.searchBroadMatchTitle ?: return

        searchBroadMatchTitle.text = getTitle(broadMatchDataView)
        searchBroadMatchTitle.addOnImpressionListener(broadMatchDataView) {
            broadMatchListener.onBroadMatchImpressed(broadMatchDataView)
        }
    }

    private fun getTitle(broadMatchDataView: BroadMatchDataView) =
            broadMatchDataView.keyword +
                    if (broadMatchDataView.isAppendTitleInTokopedia)
                        " " + getString(R.string.broad_match_in_tokopedia)
                    else ""

    private fun bindSeeMore(broadMatchDataView: BroadMatchDataView) {
        binding?.searchBroadMatchSeeMore?.showWithCondition(broadMatchDataView.applink.isNotEmpty())

        binding?.searchBroadMatchSeeMore?.setOnClickListener {
            broadMatchListener.onBroadMatchSeeMoreClicked(broadMatchDataView)
        }
    }

    private fun setupRecyclerView(dataData: BroadMatchDataView){
        val products = dataData.broadMatchItemDataViewList
        broadMatchListener.productCardLifecycleObserver?.let {
            binding?.searchBroadMatchList?.productCardLifecycleObserver = it
        }

        binding?.searchBroadMatchList?.bindCarouselProductCardViewGrid(
            recyclerViewPool = broadMatchListener.carouselRecycledViewPool,
            productCardModelList = products.map {
                ProductCardModel(
                    productName = it.name,
                    formattedPrice = it.priceString,
                    productImageUrl = it.imageUrl,
                    countSoldRating = it.ratingAverage,
                    labelGroupList = it.labelGroupDataList.toProductCardLabelGroup(),
                    shopLocation = if (it.shopLocation.isNotEmpty()) it.shopLocation else it.shopName,
                    shopBadgeList = it.badgeItemDataViewList.toProductCardModelShopBadges(),
                    freeOngkir = it.freeOngkirDataView.toProductCardModelFreeOngkir(),
                    isTopAds = it.isOrganicAds,
                    hasThreeDots = it.carouselProductType.hasThreeDots,
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

    private fun List<BadgeItemDataView>?.toProductCardModelShopBadges(): List<ProductCardModel.ShopBadge> {
        return this?.map {
            ProductCardModel.ShopBadge(it.isShown, it.imageUrl)
        } ?: listOf()
    }

    private fun List<LabelGroupDataView>?.toProductCardLabelGroup(): List<ProductCardModel.LabelGroup> {
        return this?.map {
            ProductCardModel.LabelGroup(
                    title = it.title,
                    position = it.position,
                    type = it.type,
                    imageUrl = it.imageUrl
            )
        } ?: listOf()
    }

    private fun FreeOngkirDataView.toProductCardModelFreeOngkir(): ProductCardModel.FreeOngkir {
        return ProductCardModel.FreeOngkir(isActive, imageUrl)
    }
}