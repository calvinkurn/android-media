package com.tokopedia.topads.dashboard.recommendation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.dashboard.recommendation.common.InsightConstants.AD_TYPE_PRODUCT
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.ItemListTypeFactory

data class ItemListUiModel(
    val adType:Int = AD_TYPE_PRODUCT,
    val groupId: String = "",
    val title: String = "",
    var isSelected: Boolean = false
)
    : Visitable<ItemListTypeFactory> {
    override fun type(typeFactory: ItemListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
