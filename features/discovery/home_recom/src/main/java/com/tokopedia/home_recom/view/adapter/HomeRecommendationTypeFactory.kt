package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel

interface HomeRecommendationTypeFactory {
    fun type(dataModel: ProductInfoDataModel): Int
    fun type(dataModel: RecommendationScrollDataModel): Int
    fun type(dataModel: RecommendationCarouselDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}