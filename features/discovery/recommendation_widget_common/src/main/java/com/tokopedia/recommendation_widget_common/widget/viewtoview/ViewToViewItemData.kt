package com.tokopedia.recommendation_widget_common.widget.viewtoview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class ViewToViewItemData(
    val name: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val departmentId: String = "",
    val url: String = "",
    val recommendationData: RecommendationItem = RecommendationItem(),
) : ImpressHolder(), Visitable<ViewToViewItemTypeFactory> {
    override fun type(typeFactory: ViewToViewItemTypeFactory): Int {
        return typeFactory.type(this)
    }
}
