package com.tokopedia.tokopedianow.common.model.categorymenu

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowCategoryMenuUiModel(
    val id: String = String.EMPTY,
    val title: String = String.EMPTY,
    val seeAllAppLink: String = String.EMPTY,
    val categoryListUiModel: List<Visitable<*>>? = null,
    val source: String = String.EMPTY,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
): Visitable<TokoNowCategoryMenuTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowCategoryMenuTypeFactory): Int {
        return typeFactory.type(this)
    }
}
