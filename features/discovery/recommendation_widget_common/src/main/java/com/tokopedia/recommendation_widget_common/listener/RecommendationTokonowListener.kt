package com.tokopedia.recommendation_widget_common.listener

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by yfsx on 03/09/21.
 */
interface RecommendationTokonowListener {
    fun onProductTokonowNonVariantQuantityChanged(recomItem: RecommendationItem, adapterPosition: Int, quantity: Int)
    fun onProductTokonowVariantClicked(recomItem: RecommendationItem, adapterPosition: Int)
}