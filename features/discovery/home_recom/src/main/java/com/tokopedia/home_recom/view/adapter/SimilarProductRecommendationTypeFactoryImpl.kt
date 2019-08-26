package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder

/**
 * Created by Lukas on 26/08/19
 * This class extends from [BaseAdapterTypeFactory] and implement from [SimilarProductRecommendationTypeFactory]
 */
class SimilarProductRecommendationTypeFactoryImpl : BaseAdapterTypeFactory(), SimilarProductRecommendationTypeFactory {

    /**
     * This override function from [SimilarProductRecommendationTypeFactory]
     * It return viewType for [RecommendationItemDataModel]
     * @param dataModel dataModel for [RecommendationItemViewHolder]
     */
    override fun type(dataModel: RecommendationItemDataModel): Int {
        return RecommendationItemDataModel.LAYOUT
    }

    /**
     * This override function from [SimilarProductRecommendationTypeFactory]
     * It return viewType for [LoadingModel]
     * @param viewModel dataModel for [LoadingShimmeringGridViewHolder]
     */
    override fun type(viewModel: LoadingModel?): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    /**
     * This override function from [BaseAdapterTypeFactory]
     * It return viewHolder
     * @param view the parent of the view
     * @param type the type of view
     */
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationItemDataModel.LAYOUT -> RecommendationItemViewHolder(view)
            LoadingShimmeringGridViewHolder.LAYOUT -> LoadingShimmeringGridViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}