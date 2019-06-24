package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView

class RecommendationItemViewHolder(
       private val view: View
) : AbstractViewHolder<RecommendationItemDataModel>(view){

    private val productItem: RecommendationCardView by lazy { view.findViewById<RecommendationCardView>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel) {
        productItem.setRecommendationModel(element.productItem, element.listener)
    }

}