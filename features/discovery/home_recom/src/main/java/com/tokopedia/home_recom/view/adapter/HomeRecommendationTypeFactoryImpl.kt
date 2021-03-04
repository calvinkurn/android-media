package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.view.viewholder.*
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

/**
 * A Class of Implementation Type Factory Pattern.
 *
 * This class extends from [BaseAdapterTypeFactory] and implement from [HomeRecommendationTypeFactory]
 */
class HomeRecommendationTypeFactoryImpl(
        private val recommendationListener: RecommendationListener,
        private val titleListener: TitleListener,
        private val recommendationErrorListener: RecommendationErrorListener,
        private val productInfoListener: ProductInfoViewHolder.ProductInfoListener
) : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {
    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [ProductInfoDataModel]
     * @param dataModel dataModel for [ProductInfoViewHolder]
     */
    override fun type(dataModel: ProductInfoDataModel): Int {
        return ProductInfoDataModel.LAYOUT
    }

    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [RecommendationItemDataModel]
     * @param dataModel dataModel for [RecommendationItemViewHolder]
     */
    override fun type(dataModel: RecommendationItemDataModel): Int {
        return RecommendationItemDataModel.LAYOUT
    }

    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [RecommendationCarouselDataModel]
     * @param dataModel dataModel for [RecommendationCarouselViewHolder]
     */
    override fun type(dataModel: RecommendationCarouselDataModel): Int {
        return RecommendationCarouselDataModel.LAYOUT
    }

    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [TitleDataModel]
     * @param dataModel dataModel for [TitleViewHolder]
     */
    override fun type(dataModel: TitleDataModel): Int {
        return TitleDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationErrorDataModel): Int {
        return RecommendationErrorDataModel.LAYOUT
    }

    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [RecommendationCarouselItemDataModel]
     * @param dataModel dataModel for [RecommendationCarouselViewHolder]
     */
    override fun type(dataModel: RecommendationCarouselItemDataModel): Int {
        return RecommendationCarouselItemDataModel.LAYOUT
    }

    /**
     * This override function from [HomeRecommendationTypeFactory]
     * It return viewType for [LoadingModel]
     * @param viewModel dataModel for [RecommendationShimmeringViewHolder]
     */
    override fun type(viewModel: LoadingModel?): Int {
        return RecommendationShimmeringViewHolder.LAYOUT
    }

    override fun type(dataModel: RecommendationEmptyDataModel): Int = -1

    /**
     * This override function from [BaseAdapterTypeFactory]
     * It return viewHolder
     * @param view the parent of the view
     * @param type the type of view
     */
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationItemDataModel.LAYOUT -> RecommendationItemViewHolder(view, recommendationListener)
            ProductInfoDataModel.LAYOUT -> ProductInfoViewHolder(view, productInfoListener)
            RecommendationCarouselDataModel.LAYOUT -> RecommendationCarouselViewHolder(view, recommendationListener)
            TitleDataModel.LAYOUT -> TitleViewHolder(view, titleListener)
            RecommendationErrorDataModel.LAYOUT -> RecommendationErrorViewHolder(view, recommendationErrorListener)
            RecommendationShimmeringViewHolder.LAYOUT -> RecommendationShimmeringViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}