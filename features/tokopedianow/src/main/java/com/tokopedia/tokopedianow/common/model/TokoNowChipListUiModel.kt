package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListTypeFactory

data class TokoNowChipListUiModel(
    val items: List<TokoNowChipUiModel>,
) : Visitable<TokoNowChipListTypeFactory> {

    override fun type(typeFactory: TokoNowChipListTypeFactory): Int {
        return typeFactory.type(this)
    }
}