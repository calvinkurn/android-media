package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnATCNonVariantClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemClickListener
import com.tokopedia.carouselproductcard.CarouselProductCardListener.OnItemImpressedListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBroadmatchBinding
import com.tokopedia.tokopedianow.search.presentation.listener.BroadMatchListener
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToLabelGroup
import com.tokopedia.tokopedianow.searchcategory.presentation.mapper.mapToNonVariant
import com.tokopedia.utils.view.binding.viewBinding

class BroadMatchViewHolder(
    itemView: View,
    private val broadMatchListener: BroadMatchListener
): AbstractViewHolder<BroadMatchDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_broadmatch
    }

    private var binding: ItemTokopedianowBroadmatchBinding? by viewBinding()

    private val titleView by lazy {
        binding?.tokopediaNowBroadMatchTitle
    }

    private val seeAllView by lazy {
        binding?.tokopediaNowBroadMatchSeeAll
    }

    private val carouselProductCardView by lazy {
        binding?.tokopediaNowBroadMatchCarouselProductCard
    }

    override fun bind(element: BroadMatchDataView?) {
        element ?: return

        bindTitle(element)
        bindSeeMore(element)
        bindCarousel(element)
    }

    private fun bindTitle(element: BroadMatchDataView) {
        titleView?.text = element.keyword
    }

    private fun bindSeeMore(element: BroadMatchDataView) {
        seeAllView?.shouldShowWithAction(element.applink.isNotEmpty()) {
            seeAllView?.setOnClickListener {
                broadMatchListener.onBroadMatchSeeAllClicked(element)
            }
        }
    }

    private fun bindCarousel(element: BroadMatchDataView) {
        val broadMatchItemList = element.broadMatchItemDataViewList
        val productCardModelList = broadMatchItemList.mapToProductCardModel()

        carouselProductCardView?.bindCarouselProductCardViewGrid(
            productCardModelList = productCardModelList,
            recyclerViewPool = broadMatchListener.getRecyclerViewPool(),
            scrollToPosition = broadMatchListener.onGetCarouselScrollPosition(adapterPosition),
            showSeeMoreCard = false,
            carouselProductCardOnItemClickListener = object: OnItemClickListener {
                override fun onItemClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                ) {
                    val broadMatchItem = broadMatchItemList
                        .getOrNull(carouselProductCardPosition) ?: return

                    broadMatchListener.onBroadMatchItemClicked(broadMatchItem)
                }
            },
            carouselProductCardOnItemImpressedListener = object: OnItemImpressedListener {
                override fun onItemImpressed(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                ) {
                    val broadMatchItem = broadMatchItemList
                        .getOrNull(carouselProductCardPosition) ?: return

                    broadMatchListener.onBroadMatchItemImpressed(broadMatchItem)
                }

                override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                    return broadMatchItemList.getOrNull(carouselProductCardPosition)
                }
            },
            carouselProductCardOnItemATCNonVariantClickListener = object: OnATCNonVariantClickListener {
                override fun onATCNonVariantClick(
                    productCardModel: ProductCardModel,
                    carouselProductCardPosition: Int,
                    quantity: Int,
                ) {
                    val broadMatchItem = broadMatchItemList
                        .getOrNull(carouselProductCardPosition) ?: return

                    saveCarouselScrollPosition()

                    broadMatchListener.onBroadMatchItemATCNonVariant(
                        broadMatchItem,
                        quantity,
                        adapterPosition,
                    )
                }
            },
        )
    }

    private fun List<BroadMatchItemDataView>.mapToProductCardModel() = map {
        ProductCardModel(
            productName = it.name,
            formattedPrice = it.priceString,
            productImageUrl = it.imageUrl,
            countSoldRating = it.ratingAverage,
            labelGroupList = it.labelGroupDataList.mapToLabelGroup(),
            nonVariant = it.nonVariantATC?.mapToNonVariant(),
        )
    }

    private fun saveCarouselScrollPosition() {
        val adapterPosition = this.adapterPosition
        val carouselScrollPosition = carouselProductCardView?.getCurrentPosition() ?: 0

        broadMatchListener.onSaveCarouselScrollPosition(
            adapterPosition = adapterPosition,
            scrollPosition = carouselScrollPosition,
        )
    }

    override fun onViewRecycled() {
        saveCarouselScrollPosition()
        super.onViewRecycled()
    }
}