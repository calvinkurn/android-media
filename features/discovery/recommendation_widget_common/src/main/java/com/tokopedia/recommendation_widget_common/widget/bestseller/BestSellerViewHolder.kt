package com.tokopedia.recommendation_widget_common.widget.bestseller

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.databinding.BestSellerViewHolderBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.viewutil.ChannelWidgetUtil
import com.tokopedia.recommendation_widget_common.viewutil.toDpInt
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipFilterAdapter
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.decoration.CommonMarginStartDecoration
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel.Companion.BEST_SELLER_HIDE_LOADING_RECOMMENDATION
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel.Companion.BEST_SELLER_UPDATE_RECOMMENDATION
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselAdapter
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactoryImpl
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener

/**
 * Created by Lukas on 05/11/20.
 */
class BestSellerViewHolder (private val view: View,
                            private val listener: RecommendationWidgetListener,
                            cardInteraction: Boolean = false
): AbstractViewHolder<BestSellerDataModel>(view), AnnotationChipListener, RecommendationCarouselListener{

    private val minChipsToShow = 1

    private var recommendationTypeFactory = RecommendationCarouselTypeFactoryImpl(this, cardInteraction)

    private var annotationChipAdapter: AnnotationChipFilterAdapter = AnnotationChipFilterAdapter(this)
    private var recommendationAdapter: RecommendationCarouselAdapter = RecommendationCarouselAdapter(recommendationTypeFactory)

    private var binding: BestSellerViewHolderBinding? by viewBinding()

    private var bestSellerDataModel: BestSellerDataModel? = null
    init {
        view.hide()
    }

    override fun bind(element: BestSellerDataModel) {
        if(element.recommendationItemList.isNotEmpty()) {
            bestSellerDataModel = element
            initHeader(element)
            initFilterChip(element)
            initRecommendation(element)
            setChannelDivider(element)
        }
    }

    override fun bind(element: BestSellerDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && payloads.first() is Bundle){
            val bundle = payloads.first() as Bundle
            if(bundle.containsKey(BEST_SELLER_UPDATE_RECOMMENDATION)){
                bestSellerDataModel = element
                initHeader(element)
                initFilterChip(element)
                initRecommendation(element)
                binding?.bestSellerLoadingRecommendation?.root?.hide()
                binding?.bestSellerRecommendationRecyclerView?.show()
            } else if(bundle.containsKey(BEST_SELLER_HIDE_LOADING_RECOMMENDATION)){
                binding?.bestSellerLoadingRecommendation?.root?.hide()
                binding?.bestSellerRecommendationRecyclerView?.show()
            }
        }
    }

    private fun initHeader(element: BestSellerDataModel){
        binding?.bestSellerTitle?.shouldShowWithAction(element.title.isNotBlank()){
            binding?.bestSellerTitle?.text = element.title
        }
        binding?.bestSellerSubtitle?.shouldShowWithAction(element.subtitle.isNotBlank()){
            binding?.bestSellerSubtitle?.text = element.subtitle
            anchorSeeMoreButtonTo(R.id.best_seller_subtitle)
        }
        binding?.bestSellerSeeMore?.shouldShowWithAction(element.seeMoreAppLink.isNotBlank()){
            binding?.bestSellerSeeMore?.setOnClickListener {
                listener.onBestSellerSeeMoreTextClick(element, element.seeMoreAppLink, adapterPosition)
            }
        }
        binding?.containerBestSellerWidget?.show()
        itemView.show()
    }

    private fun initFilterChip(element: BestSellerDataModel){
        binding?.bestSellerChipFilterRecyclerview?.shouldShowWithAction(element.filterChip.size > minChipsToShow){
            if (binding?.bestSellerChipFilterRecyclerview?.adapter == null) {
                binding?.bestSellerChipFilterRecyclerview?.adapter = annotationChipAdapter
            }
            annotationChipAdapter.submitList(element.filterChip)
        }
    }

    private fun initRecommendation(element: BestSellerDataModel) {
        binding?.bestSellerRecommendationRecyclerView?.shouldShowWithAction(element.recommendationItemList.isNotEmpty()){
            if(binding?.bestSellerRecommendationRecyclerView?.adapter == null) {
                binding?.bestSellerRecommendationRecyclerView?.adapter = recommendationAdapter
            }
            if (binding?.bestSellerRecommendationRecyclerView?.itemDecorationCount == 0) {
                binding?.bestSellerRecommendationRecyclerView?.addItemDecoration(
                    CommonMarginStartDecoration(
                        marginStart = 12f.toDpInt()
                    )
                )
            }
            if (binding?.bestSellerRecommendationRecyclerView?.itemDecorationCount == 0) {
                binding?.bestSellerRecommendationRecyclerView?.addItemDecoration(
                    CommonMarginStartDecoration(
                        marginStart = 8f.toDpInt()
                    )
                )
            }
            val recommendationCarouselList: MutableList<Visitable<RecommendationCarouselTypeFactory>> = element.recommendationItemList.withIndex().map {
                RecommendationCarouselItemDataModel(it.value, element.productCardModelList[it.index])
            }.toMutableList()
            if(element.seeMoreAppLink.isNotBlank()) recommendationCarouselList.add(RecommendationSeeMoreDataModel(element.seeMoreAppLink))
            recommendationAdapter.submitList(recommendationCarouselList)

            binding?.bestSellerRecommendationRecyclerView?.show()
            binding?.bestSellerRecommendationRecyclerView?.layoutParams?.height = element.height
            binding?.bestSellerRecommendationRecyclerView?.layoutManager?.scrollToPosition(0)
        }
    }

    private fun anchorSeeMoreButtonTo(anchorRef: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding?.containerBestSellerWidget)
        constraintSet.connect(R.id.best_seller_see_more, ConstraintSet.TOP, anchorRef, ConstraintSet.TOP, 0)
        constraintSet.connect(R.id.best_seller_see_more, ConstraintSet.BOTTOM, anchorRef, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(binding?.containerBestSellerWidget)
    }

    override fun onFilterAnnotationClicked(annotationChip: RecommendationFilterChipsEntity.RecommendationFilterChip, position: Int) {
        bestSellerDataModel?.let {
            annotationChipAdapter.submitList(
                it.filterChip.map {filter ->
                    filter.copy(
                        isActivated = annotationChip.title == filter.title
                                && !annotationChip.isActivated
                    )
                }
            )
            listener.onBestSellerFilterClick(annotationChip.copy(isActivated = !annotationChip.isActivated), it, adapterPosition, position)
            binding?.bestSellerLoadingRecommendation?.root?.show()
            binding?.bestSellerRecommendationRecyclerView?.hide()
            bestSellerDataModel?.chipsPosition = (position+1)
        }
    }

    override fun onSeeMoreCardClick(applink: String) {
        bestSellerDataModel?.let { listener.onBestSellerSeeAllCardClick(it, applink, adapterPosition) }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) TopAdsUrlHitter(view.context).hitClickUrl(
            CLASS_NAME,
            item.clickUrl,
            item.productId.toString(),
            item.name,
            item.imageUrl
        )
        bestSellerDataModel?.let { listener.onBestSellerClick(it, item, adapterPosition) }
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) TopAdsUrlHitter(view.context).hitImpressionUrl(CLASS_NAME, item.trackerImageUrl, item.productId.toString(), item.name, item.imageUrl)
        bestSellerDataModel?.let { listener.onBestSellerImpress(it, item, adapterPosition) }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        bestSellerDataModel?.let { listener.onBestSellerThreeDotsClick(it, item, adapterPosition) }
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {}
    override fun onWishlistV2Click(item: RecommendationItem, isAddWishlist: Boolean) {}

    companion object{
        val LAYOUT = R.layout.best_seller_view_holder
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder"
    }

    private fun setChannelDivider(element: BestSellerDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            dividerType = element.dividerType,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }
}