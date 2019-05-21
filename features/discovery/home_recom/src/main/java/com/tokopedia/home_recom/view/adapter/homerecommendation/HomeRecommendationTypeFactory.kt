package com.tokopedia.home_recom.view.adapter.homerecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.*

interface HomeRecommendationTypeFactory {
    fun type(dataModel: ProductInfoDataModel): Int
    fun type(dataModel: RecommendationItemDataModel): Int
    fun type(dataModel: RecommendationCarouselDataModel): Int
    fun type(dataModel: RecommendationAnotherProductItemDataModel): Int
    fun type(dataModel: TitleDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}