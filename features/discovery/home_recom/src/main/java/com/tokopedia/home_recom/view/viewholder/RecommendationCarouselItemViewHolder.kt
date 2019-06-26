package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView

class RecommendationCarouselItemViewHolder (
        private val view: View
) : AbstractViewHolder<RecommendationCarouselItemDataModel>(view){

    private val productItem: RecommendationCardView by lazy { view.findViewById(R.id.product_item) }

    override fun bind(element: RecommendationCarouselItemDataModel) {
        productItem.setRecommendationModel(element.productItem, element.listener)
    }

}