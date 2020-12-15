package com.tokopedia.buyerorder.list.view.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.list.view.adapter.factory.OrderListTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class OrderListRecomUiModel(val recommendationItem: RecommendationItem, val recomTitle: String) : Visitable<OrderListTypeFactory> {

    override fun type(typeFactory: OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
