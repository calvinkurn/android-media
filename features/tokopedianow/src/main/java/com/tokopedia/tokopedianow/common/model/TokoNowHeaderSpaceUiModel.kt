package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowHeaderSpaceTypeFactory

data class TokoNowHeaderSpaceUiModel(
    val id: String = String.EMPTY,
    val space: Int = Int.ZERO,
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY,
    val backgroundColor: Int? = null
): Visitable<TokoNowHeaderSpaceTypeFactory> {
    override fun type(typeFactory: TokoNowHeaderSpaceTypeFactory): Int = typeFactory.type(this)
}

