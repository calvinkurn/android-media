package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationCarouselItemDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.model.RecommendationSeeMoreDataModel

/**
 * Created by Lukas on 05/11/20.
 */

interface RecommendationCarouselTypeFactory {
    fun type(dataModel: RecommendationSeeMoreDataModel) : Int
    fun type(dataModel: RecommendationCarouselItemDataModel) : Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<Visitable<RecommendationCarouselTypeFactory>>
}
