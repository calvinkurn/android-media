package com.tokopedia.home_recom.view.adapter.homerecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel
import com.tokopedia.home_recom.view.viewHolder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationItemViewHolder

class HomeRecommendationTypeFactoryImpl : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {
    override fun type(dataModel: ProductInfoDataModel): Int {
        return ProductInfoDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationScrollDataModel): Int {
        return RecommendationScrollDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationCarouselDataModel): Int {
        return RecommendationCarouselDataModel.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationScrollDataModel.LAYOUT -> RecommendationItemViewHolder(view)
            ProductInfoDataModel.LAYOUT -> ProductInfoViewHolder(view)
            RecommendationCarouselDataModel.LAYOUT -> RecommendationCarouselViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }


}