package com.tokopedia.topads.dashboard.recommendation.data.model.local

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

data class ListBottomSheetItemUiModel(
    val adType:Int = TYPE_PRODUCT_VALUE,
    val groupId: String = "",
    val title: String = "",
    var isSelected: Boolean = false
)
    : Visitable<ItemListTypeFactory> {
    override fun type(typeFactory: ItemListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
