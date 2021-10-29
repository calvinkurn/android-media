package com.tokopedia.tokopedianow.common.model

import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

data class TokoNowEmptyStateOocUiModel (
        val id: String = "",
        val eventCategory: String = "",
        val hostSource: String = ""
) : TokoNowLayoutUiModel(id) {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}