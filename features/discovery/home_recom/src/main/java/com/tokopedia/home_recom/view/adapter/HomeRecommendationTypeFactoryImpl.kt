package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.datamodel.TitleDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.view.viewholder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselItemViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationItemViewHolder
import com.tokopedia.home_recom.view.viewholder.TitleViewHolder

class HomeRecommendationTypeFactoryImpl : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {
    override fun type(dataModel: ProductInfoDataModel): Int {
        return ProductInfoDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationItemDataModel): Int {
        return RecommendationItemDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationCarouselDataModel): Int {
        return RecommendationCarouselDataModel.LAYOUT
    }

    override fun type(dataModel: TitleDataModel): Int {
        return TitleDataModel.LAYOUT
    }

    override fun type(dataModel: RecommendationCarouselItemDataModel): Int {
        return RecommendationCarouselItemDataModel.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationItemDataModel.LAYOUT -> RecommendationItemViewHolder(view)
            ProductInfoDataModel.LAYOUT -> ProductInfoViewHolder(view)
            RecommendationCarouselDataModel.LAYOUT -> RecommendationCarouselViewHolder(view)
            TitleDataModel.LAYOUT -> TitleViewHolder(view)
            RecommendationItemDataModel.LAYOUT -> RecommendationCarouselItemViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }


}