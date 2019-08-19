package com.tokopedia.product.detail.data.model.addtocartrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.RecommendationProductTypeFactory
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class RecommendationProductViewModel(
        val recommendationItem: RecommendationItem
) : Visitable<RecommendationProductTypeFactory>{
    override fun type(productTypeFactory: RecommendationProductTypeFactory): Int {
        return productTypeFactory.type(this)
    }

}