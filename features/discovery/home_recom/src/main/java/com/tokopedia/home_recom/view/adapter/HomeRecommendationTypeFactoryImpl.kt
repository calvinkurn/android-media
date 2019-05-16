package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel
import com.tokopedia.home_recom.view.viewHolder.RecommendationScrollViewHolder

class HomeRecommendationTypeFactoryImpl : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory{
    override fun type(dataModel: ProductInfoDataModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(dataModel: RecommendationScrollDataModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun type(dataModel: RecommendationCarouselDataModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type){
            RecommendationScrollDataModel.LAYOUT -> RecommendationScrollViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }


}