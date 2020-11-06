package com.tokopedia.recommendation_widget_common.widget.bestseller

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipFilterAdapter
import com.tokopedia.recommendation_widget_common.widget.bestseller.annotationfilter.AnnotationChipListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselAdapter
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter.RecommendationCarouselListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory.RecommendationCarouselTypeFactoryImpl
import kotlinx.android.synthetic.main.best_seller_view_holder.view.*

/**
 * Created by Lukas on 05/11/20.
 */
class BestSellerViewHolder (view: View, private val listener: RecommendationWidgetListener): AbstractViewHolder<BestSellerDataModel>(view), AnnotationChipListener, RecommendationCarouselListener{
    private var recommendationTypeFactory = RecommendationCarouselTypeFactoryImpl(this)

    private var annotationChipAdapter: AnnotationChipFilterAdapter = AnnotationChipFilterAdapter(this)
    private var recommendationAdapter: RecommendationCarouselAdapter = RecommendationCarouselAdapter(recommendationTypeFactory)

    private var bestSellerDataModel: BestSellerDataModel? = null

    override fun bind(element: BestSellerDataModel) {
        bestSellerDataModel = element
        initFilterChip(element)
        initRecommendation(element)
    }

    private fun initFilterChip(element: BestSellerDataModel){
        if (itemView.best_seller_chip_filter_recyclerview?.adapter == null) {
            itemView.best_seller_chip_filter_recyclerview?.adapter = annotationChipAdapter
        }
        annotationChipAdapter.submitList(element.filterChip)
    }

    private fun initRecommendation(element: BestSellerDataModel) {
        if(itemView.best_seller_recommendation_recycler_view.adapter == null) {
            itemView.best_seller_recommendation_recycler_view.adapter = recommendationAdapter
        }
        val recommendationCarouselList: MutableList<Visitable<RecommendationCarouselTypeFactory>> = element.recommendationItemList.withIndex().map {
            RecommendationCarouselItemDataModel(it.value, element.productCardModelList[it.index])
        }.toMutableList()

        recommendationCarouselList.add(RecommendationSeeMoreDataModel(element.seeMoreAppLink))

        recommendationAdapter.submitList(recommendationCarouselList)
    }

    override fun onFilterAnnotationClicked(annotationChip: RecommendationFilterChipsEntity.RecommendationFilterChip, position: Int) {
        bestSellerDataModel?.let {
            listener.onBestSellerFilterClick(annotationChip.copy(isActivated = !annotationChip.isActivated), it, adapterPosition)
            itemView.best_seller_loading_recommendation.show()
            itemView.best_seller_recommendation_recycler_view.hide()
        }
    }

    override fun onSeeMoreCardClick(applink: String) {
        bestSellerDataModel?.let { listener.onBestSellerSeeAllCardClick(it, applink, adapterPosition) }
    }

    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        bestSellerDataModel?.let { listener.onBestSellerClick(it, item, adapterPosition) }
    }

    override fun onProductImpression(item: RecommendationItem) {
        bestSellerDataModel?.let { listener.onBestSellerImpress(it, item, adapterPosition) }
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {}

    companion object{
        val LAYOUT = R.layout.best_seller_view_holder
    }

}