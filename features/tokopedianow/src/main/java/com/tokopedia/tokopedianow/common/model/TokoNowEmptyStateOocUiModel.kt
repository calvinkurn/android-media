package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowEmptyStateOocTypeFactory

data class TokoNowEmptyStateOocUiModel (
        val id: String = "",
        val eventCategory: String = ""
) : Visitable<TokoNowEmptyStateOocTypeFactory> {
    override fun type(typeFactory: TokoNowEmptyStateOocTypeFactory): Int {
        return typeFactory.type(this)
    }
}