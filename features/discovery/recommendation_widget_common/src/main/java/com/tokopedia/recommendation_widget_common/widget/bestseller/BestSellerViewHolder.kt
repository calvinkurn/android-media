package com.tokopedia.recommendation_widget_common.widget.bestseller

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
import kotlinx.android.synthetic.main.best_seller_view_holder.view.*
import kotlinx.android.synthetic.main.best_seller_view_holder.view.home_component_divider_footer
import kotlinx.android.synthetic.main.best_seller_view_holder.view.home_component_divider_header
import kotlinx.android.synthetic.main.best_seller_view_holder.view.home_component_header_view

/**
 * Created by Lukas on 05/11/20.
 */
class BestSellerViewHolder (view: View, private val listener: RecommendationWidgetListener): AbstractViewHolder<BestSellerDataModel>(view), AnnotationChipListener, RecommendationCarouselListener{

    private val minChipsToShow = 1

    private var recommendationTypeFactory = RecommendationCarouselTypeFactoryImpl(this)

    private var annotationChipAdapter: AnnotationChipFilterAdapter = AnnotationChipFilterAdapter(this)
    private var recommendationAdapter: RecommendationCarouselAdapter = RecommendationCarouselAdapter(recommendationTypeFactory)

    private var bestSellerDataModel: BestSellerDataModel? = null
    init {
        view.hide()
    }

    override fun bind(element: BestSellerDataModel) {
        if(element.recommendationItemList.isNotEmpty()) {
            bestSellerDataModel = element
            initHeader(element)
            setChannelDivider(element)
            initFilterChip(element)
            initRecommendation(element)
        }
    }

    override fun bind(element: BestSellerDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && payloads.first() is Bundle){
            val bundle = payloads.first() as Bundle
            if(bundle.containsKey(BEST_SELLER_UPDATE_RECOMMENDATION)){
                bestSellerDataModel = element
                initHeader(element)
                setChannelDivider(element)
                initFilterChip(element)
                initRecommendation(element)
                itemView.best_seller_loading_recommendation.hide()
                itemView.best_seller_recommendation_recycler_view.show()
            } else if(bundle.containsKey(BEST_SELLER_HIDE_LOADING_RECOMMENDATION)){
                itemView.best_seller_loading_recommendation.hide()
                itemView.best_seller_recommendation_recycler_view.show()
            }
        }
    }

    private fun initHeader(element: BestSellerDataModel){
        itemView.home_component_header_view.setChannel(element.channelModel, object :
            HeaderListener {
            override fun onSeeAllClick(link: String) {
                listener.onBestSellerSeeMoreTextClick(element, element.seeMoreAppLink, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
        itemView.container_best_seller_widget.show()
        itemView.show()
    }

    private fun initFilterChip(element: BestSellerDataModel){
        itemView.best_seller_chip_filter_recyclerview.shouldShowWithAction(element.filterChip.size > minChipsToShow){
            if (itemView.best_seller_chip_filter_recyclerview?.adapter == null) {
                itemView.best_seller_chip_filter_recyclerview?.adapter = annotationChipAdapter
            }
            annotationChipAdapter.submitList(element.filterChip)
        }
    }

    private fun initRecommendation(element: BestSellerDataModel) {
        itemView.best_seller_recommendation_recycler_view.shouldShowWithAction(element.recommendationItemList.isNotEmpty()){
            if(itemView.best_seller_recommendation_recycler_view.adapter == null) {
                itemView.best_seller_recommendation_recycler_view.adapter = recommendationAdapter
            }
            if (itemView.best_seller_recommendation_recycler_view.itemDecorationCount == 0) {
                itemView.best_seller_recommendation_recycler_view.addItemDecoration(
                        CommonMarginStartDecoration(
                                marginStart = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_12)
                        )
                )
            }
            if (itemView.best_seller_chip_filter_recyclerview.itemDecorationCount == 0) {
                itemView.best_seller_chip_filter_recyclerview.addItemDecoration(
                        CommonMarginStartDecoration(
                                marginStart = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_8)
                        )
                )
            }
            val recommendationCarouselList: MutableList<Visitable<RecommendationCarouselTypeFactory>> = element.recommendationItemList.withIndex().map {
                RecommendationCarouselItemDataModel(it.value, element.productCardModelList[it.index])
            }.toMutableList()
            if(element.seeMoreAppLink.isNotBlank()) recommendationCarouselList.add(RecommendationSeeMoreDataModel(element.seeMoreAppLink))
            recommendationAdapter.submitList(recommendationCarouselList)

            itemView.best_seller_recommendation_recycler_view.show()
            itemView.best_seller_recommendation_recycler_view.layoutParams.height = element.height
            itemView.best_seller_recommendation_recycler_view.layoutManager?.scrollToPosition(0)
        }
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
            itemView.best_seller_loading_recommendation.show()
            itemView.best_seller_recommendation_recycler_view.hide()
            bestSellerDataModel?.chipsPosition = (position+1)
        }
    }

    override fun onSeeMoreCardClick(applink: String) {
        bestSellerDataModel?.let { listener.onBestSellerSeeAllCardClick(it, applink, adapterPosition) }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        if (item.isTopAds) TopAdsUrlHitter(itemView.context).hitClickUrl(
                CLASS_NAME,
                item.clickUrl,
                item.productId.toString(),
                item.name,
                item.imageUrl
        )
        bestSellerDataModel?.let { listener.onBestSellerClick(it, item, adapterPosition) }
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) TopAdsUrlHitter(itemView.context).hitImpressionUrl(CLASS_NAME, item.trackerImageUrl, item.productId.toString(), item.name, item.imageUrl)
        bestSellerDataModel?.let { listener.onBestSellerImpress(it, item, adapterPosition) }
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        bestSellerDataModel?.let { listener.onBestSellerThreeDotsClick(it, item, adapterPosition) }
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {}

    companion object{
        val LAYOUT = R.layout.best_seller_view_holder
        private const val CLASS_NAME = "com.tokopedia.recommendation_widget_common.widget.bestseller.BestSellerViewHolder"
    }

    private fun setChannelDivider(element: BestSellerDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

}