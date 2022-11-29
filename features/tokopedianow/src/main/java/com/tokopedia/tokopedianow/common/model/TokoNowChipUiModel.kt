package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipTypeFactory

data class TokoNowChipUiModel(
    val id: String,
    val parentId: String = "",
    val text: String,
    val imageUrl: String = "",
    val selected: Boolean = false,
    val isPopular: Boolean = true
): Visitable<TokoNowChipTypeFactory> {

    override fun type(typeFactory: TokoNowChipTypeFactory): Int {
        return typeFactory.type(this)
    }
}