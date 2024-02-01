package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowDividerTypeFactory

data class TokoNowDividerUiModel(
    val id: String = String.EMPTY
): Visitable<TokoNowDividerTypeFactory> {
    override fun type(typeFactory: TokoNowDividerTypeFactory): Int = typeFactory.type(this)
}
