package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowLocalLoadTypeFactory

data class TokoNowLocalLoadUiModel(
    val id: String = String.EMPTY
): Visitable<TokoNowLocalLoadTypeFactory> {
    override fun type(typeFactory: TokoNowLocalLoadTypeFactory): Int = typeFactory.type(this)
}
