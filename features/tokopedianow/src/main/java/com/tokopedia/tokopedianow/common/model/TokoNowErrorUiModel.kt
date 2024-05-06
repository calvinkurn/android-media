package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowErrorTypeFactory

data class TokoNowErrorUiModel(
    val id: String = String.EMPTY,
    val throwable: Throwable,
    val isFullPage: Boolean
) : Visitable<TokoNowErrorTypeFactory> {
    override fun type(typeFactory: TokoNowErrorTypeFactory): Int = typeFactory.type(this)
}
