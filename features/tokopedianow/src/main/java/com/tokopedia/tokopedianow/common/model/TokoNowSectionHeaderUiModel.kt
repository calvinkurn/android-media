package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowSectionHeaderTypeFactory

data class TokoNowSectionHeaderUiModel(
    val id: String = "",
    val title: String,
    val seeAllAppLink: String = ""
): Visitable<TokoNowSectionHeaderTypeFactory> {

    override fun type(typeFactory: TokoNowSectionHeaderTypeFactory): Int {
        return typeFactory.type(this)
    }
}