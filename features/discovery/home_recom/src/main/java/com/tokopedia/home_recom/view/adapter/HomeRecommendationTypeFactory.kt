package com.tokopedia.home_recom.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.datamodel.*

/**
 * A Interface of Type Factory Pattern.
 *
 * This interface initialize all viewType it will shown at Adapter
 */
interface HomeRecommendationTypeFactory {
    fun type(dataModel: ProductInfoDataModel): Int
    fun type(dataModel: RecommendationItemDataModel): Int
    fun type(dataModel: RecommendationCarouselDataModel): Int
    fun type(dataModel: RecommendationCarouselItemDataModel): Int
    fun type(dataModel: TitleDataModel): Int
    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}