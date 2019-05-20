package com.tokopedia.home_recom.view.adapter.homerecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationItemDataModel
import com.tokopedia.home_recom.model.dataModel.TitleDataModel
import com.tokopedia.home_recom.view.viewHolder.ProductInfoViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationCarouselViewHolder
import com.tokopedia.home_recom.view.viewHolder.RecommendationItemViewHolder
import com.tokopedia.home_recom.view.viewHolder.TitleViewHolder

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

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationItemDataModel.LAYOUT -> RecommendationItemViewHolder(view)
            ProductInfoDataModel.LAYOUT -> ProductInfoViewHolder(view)
            RecommendationCarouselDataModel.LAYOUT -> RecommendationCarouselViewHolder(view)
            TitleDataModel.LAYOUT -> TitleViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }


}