package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowBundleTypeFactory

data class TokoNowBundleUiModel(
    val id: String = "",
    val title: String = "",
    val bundleIds: List<String> = emptyList()
) : Visitable<TokoNowBundleTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: TokoNowBundleTypeFactory): Int = typeFactory.type(this)
}
