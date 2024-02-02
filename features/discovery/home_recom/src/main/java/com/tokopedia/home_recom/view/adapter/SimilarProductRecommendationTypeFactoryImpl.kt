package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.view.viewholder.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

/**
 * Created by Lukas on 26/08/19
 * This class extends from [BaseAdapterTypeFactory] and implement from [HomeRecommendationTypeFactory]
 */
class SimilarProductRecommendationTypeFactoryImpl (
    private val recommendationListener: RecommendationListener,
    private val recommendationErrorListener: RecommendationErrorListener,
    private val recommendationEmptyStateListener: RecommendationEmptyViewHolder.RecommendationEmptyStateListener
): BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {

    override fun type(dataModel: ProductInfoDataModel): Int = -1

    override fun type(dataModel: RecommendationItemDataModel): Int = RecommendationItemDataModel.layout(true)

    override fun type(dataModel: RecommendationCarouselDataModel): Int = -1

    override fun type(dataModel: RecommendationCarouselItemDataModel): Int = -1

    override fun type(dataModel: TitleDataModel): Int = -1

    override fun type(dataModel: RecommendationErrorDataModel): Int = RecommendationErrorDataModel.LAYOUT

    override fun type(viewModel: LoadingMoreModel?): Int = SimilarProductLoadMoreViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel?): Int = LoadingShimmeringGridViewHolder.LAYOUT

    override fun type(dataModel: RecommendationEmptyDataModel): Int = RecommendationEmptyDataModel.LAYOUT

    override fun type(dataModel: RecommendationCPMDataModel): Int = -1

    override fun type(dataModel: LoadMoreDataModel): Int {
        return LoadMoreViewHolder.LAYOUT
    }

    override fun type(dataModel: FirstLoadDataModel): Int {
        return FirstLoadViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            RecommendationItemDataModel.layout(REIMAGINE_ENABLED) -> RecommendationItemViewHolder(view, recommendationListener)
            LoadingShimmeringGridViewHolder.LAYOUT -> LoadingShimmeringGridViewHolder(view)
            SimilarProductLoadMoreViewHolder.LAYOUT -> SimilarProductLoadMoreViewHolder(view)
            RecommendationErrorDataModel.LAYOUT -> RecommendationErrorViewHolder(view, recommendationErrorListener)
            RecommendationEmptyDataModel.LAYOUT -> RecommendationEmptyViewHolder(view, recommendationEmptyStateListener)
            LoadMoreViewHolder.LAYOUT -> LoadMoreViewHolder(view)
            FirstLoadViewHolder.LAYOUT -> FirstLoadViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }

    companion object {
        private const val REIMAGINE_ENABLED = true
    }
}
