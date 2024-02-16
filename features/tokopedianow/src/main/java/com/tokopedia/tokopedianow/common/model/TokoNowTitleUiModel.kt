package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTitleTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowTitleUiModel(
    val id: String = String.EMPTY,
    val title: String = String.EMPTY,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.SHOW
): Visitable<TokoNowTitleTypeFactory> {
    override fun type(typeFactory: TokoNowTitleTypeFactory): Int = typeFactory.type(this)
}
