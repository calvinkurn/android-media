package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProgressBarTypeFactory

data class TokoNowProgressBarUiModel(
    val id: String
): Visitable<TokoNowProgressBarTypeFactory> {
    override fun type(typeFactory: TokoNowProgressBarTypeFactory): Int = typeFactory.type(this)
}
