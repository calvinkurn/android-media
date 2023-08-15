package com.tokopedia.search.result.product.broadmatch

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselViewAllCardData
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductBroadMatchLayoutBinding
import com.tokopedia.search.result.presentation.model.BadgeItemDataView
import com.tokopedia.search.result.presentation.model.FreeOngkirDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.utils.convertToChannelHeader
import com.tokopedia.utils.view.binding.viewBinding

class BroadMatchViewHolder(
        itemView: View,
        private val broadMatchListener: BroadMatchListener,
        private val recycledViewPool: RecyclerView.RecycledViewPool,
        private val isReimagine: Boolean = false,
) : AbstractViewHolder<BroadMatchDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_broad_match_layout
    }
    
    private var binding: SearchResultProductBroadMatchLayoutBinding? by viewBinding()

    override fun bind(element: BroadMatchDataView) {
        if (isReimagine) {
            showHeaderRevamp()
            hideOldHeader()
            bindHeaderViewRevamp(element)
            setSearchBroadMatchListConstraintToRevampHeader()
            erasePaddingOnContainerBroadMatchListConstraint()
        } else {
            hideHeaderRevamp()
            showOldHeader()
            bindTitle(element)
            bindSubtitle(element)
            bindSubtitleImage(element)
            bindSeeMore(element)
            setSearchBroadMatchListConstraintToOldHeader()
            restorePaddingOnContainerBroadMatchListConstraint()
        }
        setupRecyclerView(element)
    }

    private fun bindHeaderViewRevamp(broadMatchDataView: BroadMatchDataView) {
        val headerView = binding?.componentHeaderView ?: return
        headerView.bind(
            channelHeader = broadMatchDataView.convertToChannelHeader(headerView.context ?: return),
            listener = object : HomeChannelHeaderListener {
                override fun onSeeAllClick(link: String) {
                    broadMatchListener.onBroadMatchSeeMoreClicked(broadMatchDataView)
                }
            }
        )
        headerView.addOnImpressionListener(broadMatchDataView) {
            broadMatchListener.onBroadMatchImpressed(broadMatchDataView)
        }
    }

    private fun showHeaderRevamp() {
        val headerView = binding?.componentHeaderView ?: return
        headerView.visible()
    }

    private fun hideHeaderRevamp() {
        val headerView = binding?.componentHeaderView ?: return
        headerView.hide()
    }

    private fun hideOldHeader() {
        binding?.searchBroadMatchTitle?.gone()
        binding?.searchBroadMatchSubtitle?.gone()
        binding?.searchBroadMatchSubtitleIcon?.gone()
        binding?.searchBroadMatchSeeMore?.gone()
    }

    private fun showOldHeader() {
        binding?.searchBroadMatchTitle?.visible()
        binding?.searchBroadMatchSubtitle?.visible()
        binding?.searchBroadMatchSubtitleIcon?.visible()
        binding?.searchBroadMatchSeeMore?.visible()
    }

    private fun setSearchBroadMatchListConstraintToRevampHeader() {
        val constraintContainerBroadMatchView = binding?.constraintContainerBroadMatchView ?: return
        val recyclerViewBroadMatchList = binding?.searchBroadMatchList ?: return
        val headerViewBroadMatchList = binding?.componentHeaderView ?: return
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintContainerBroadMatchView)
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.TOP, headerViewBroadMatchList.id, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.START, constraintContainerBroadMatchView.id, ConstraintSet.START, 0)
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.END, constraintContainerBroadMatchView.id, ConstraintSet.END, 0)
        constraintSet.applyTo(constraintContainerBroadMatchView)
    }

    private fun setSearchBroadMatchListConstraintToOldHeader() {
        val constraintContainerBroadMatchView = binding?.constraintContainerBroadMatchView ?: return
        val recyclerViewBroadMatchList = binding?.searchBroadMatchList ?: return
        val headerViewBroadMatchList = binding?.searchBroadMatchSubtitle ?: return
        val resource = recyclerViewBroadMatchList.context.resources
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintContainerBroadMatchView)
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.TOP, headerViewBroadMatchList.id, ConstraintSet.BOTTOM, resource.getDimensionPixelOffset(R.dimen.search_margin_top_inspiration_layout))
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.START, constraintContainerBroadMatchView.id, ConstraintSet.START, 0)
        constraintSet.connect(recyclerViewBroadMatchList.id, ConstraintSet.END, constraintContainerBroadMatchView.id, ConstraintSet.END, 0)
        constraintSet.applyTo(constraintContainerBroadMatchView)
    }

    private fun erasePaddingOnContainerBroadMatchListConstraint() {
        val constraintContainerBroadMatchView = binding?.constraintContainerBroadMatchView ?: return
        constraintContainerBroadMatchView.setPadding(0,0,0,0)
    }

    private fun restorePaddingOnContainerBroadMatchListConstraint() {
        val constraintContainerBroadMatchView = binding?.constraintContainerBroadMatchView ?: return
        val resource = constraintContainerBroadMatchView.context.resources
        constraintContainerBroadMatchView.setPadding(
            0,
            resource.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4),
            0,
            resource.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
        )
    }

    private fun bindTitle(broadMatchDataView: BroadMatchDataView) {
        val searchBroadMatchTitle = binding?.searchBroadMatchTitle ?: return

        searchBroadMatchTitle.text = getTitle(broadMatchDataView)
        searchBroadMatchTitle.addOnImpressionListener(broadMatchDataView) {
            broadMatchListener.onBroadMatchImpressed(broadMatchDataView)
        }
    }

    private fun bindSubtitle(broadMatchDataView: BroadMatchDataView) {
        val searchBroadMatchSubtitle = binding?.searchBroadMatchSubtitle ?: return

        searchBroadMatchSubtitle.shouldShowWithAction(broadMatchDataView.subtitle.isNotEmpty()) {
            searchBroadMatchSubtitle.text = broadMatchDataView.subtitle
        }
    }

    private fun bindSubtitleImage(broadMatchDataView: BroadMatchDataView) {
        val searchBroadMatchSubtitleIcon = binding?.searchBroadMatchSubtitleIcon ?: return

        searchBroadMatchSubtitleIcon.shouldShowWithAction(broadMatchDataView.iconSubtitle.isNotEmpty()) {
            searchBroadMatchSubtitleIcon.loadImage(broadMatchDataView.iconSubtitle)
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

    private fun setupRecyclerView(dataView: BroadMatchDataView){
        val products = dataView.broadMatchItemDataViewList
        val viewAllCardData: CarouselViewAllCardData? =
            if (dataView.cardButton.title.isNotEmpty())
                CarouselViewAllCardData(dataView.cardButton.title)
            else null
        broadMatchListener.productCardLifecycleObserver?.let {
            binding?.searchBroadMatchList?.productCardLifecycleObserver = it
        }

        binding?.searchBroadMatchList?.bindCarouselProductCardViewGrid(
            recyclerViewPool = recycledViewPool,
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
                    cardInteraction = true,
                    discountPercentage = if (it.discountPercentage > 0) "${it.discountPercentage}%"
                                         else "",
                    slashedPrice = if (it.discountPercentage > 0) it.originalPrice else "",
                    stockBarPercentage = it.stockBarDataView.percentageValue,
                    stockBarLabel = it.stockBarDataView.value,
                    stockBarLabelColor = it.stockBarDataView.color,
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
            },
            carouselViewAllCardData = viewAllCardData,
            carouselViewAllCardClickListener = object: CarouselProductCardListener.OnViewAllCardClickListener {
                override fun onViewAllCardClick() {
                    broadMatchListener.onBroadMatchViewAllCardClicked(dataView)
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
