package com.tokopedia.tokopedianow.common.model.categorymenu

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowCategoryMenuUiModel(
    val id: String = "",
    val title: String = "",
    val seeAllAppLink: String = "",
    val categoryListUiModel: List<Visitable<*>>? = null,
    @TokoNowLayoutState val state: Int
): Visitable<TokoNowCategoryMenuTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowCategoryMenuTypeFactory): Int {
        return typeFactory.type(this)
    }
}
