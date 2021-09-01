package com.tokopedia.tokopedianow.common.model

import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

class TokoNowEmptyStateNoResultUiModel(
    val id: String = "",
    val activeFilterList: List<Option>? = null,
    val defaultTitle: String = "",
    val defaultDescription: String = ""
): TokoNowLayoutUiModel(id) {

    override fun type(typeFactory: TokoNowTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}