package com.tokopedia.product.detail.data.model.addtocartrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class AddToCartDoneRecommendationDataModel(
        val recommendationWidget: RecommendationWidget
) : Visitable<AddToCartDoneTypeFactory>{
    override fun type(typeFactory: AddToCartDoneTypeFactory): Int {
        return typeFactory.type(this)
    }

}