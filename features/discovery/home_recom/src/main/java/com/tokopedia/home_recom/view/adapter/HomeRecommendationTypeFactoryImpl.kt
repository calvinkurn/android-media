package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel
import com.tokopedia.home_recom.view.viewHolder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationScrollViewHolder

class HomeRecommendationTypeFactoryImpl : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory{
    override fun type(dataModel: ProductInfoDataModel): Int {
        return ProductInfoDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationScrollDataModel): Int {
        return RecommendationScrollDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationCarouselDataModel): Int {
        return RecommendationCarouselDataModel.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationScrollDataModel.LAYOUT -> RecommendationScrollViewHolder(parent)
            ProductInfoDataModel.LAYOUT -> ProductInfoViewHolder(parent)
            RecommendationCarouselDataModel.LAYOUT -> RecommendationCarouselViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }


}