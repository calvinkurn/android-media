package com.tokopedia.tokopedianow.common.model

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowSectionHeaderTypeFactory

open class TokoNowSectionHeaderUiModel(
    val id: String = "",
    val title: String = "",
    @StringRes
    val titleResId: Int? = null,
    val seeAllAppLink: String = ""
): Visitable<TokoNowSectionHeaderTypeFactory> {

    override fun type(typeFactory: TokoNowSectionHeaderTypeFactory): Int {
        return typeFactory.type(this)
    }
}