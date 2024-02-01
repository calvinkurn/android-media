package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTitleTypeFactory

data class TokoNowTitleUiModel(
    val id: String = String.EMPTY,
    val title: String = String.EMPTY
): Visitable<TokoNowTitleTypeFactory> {
    override fun type(typeFactory: TokoNowTitleTypeFactory): Int = typeFactory.type(this)
}
