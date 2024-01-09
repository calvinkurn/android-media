package com.tokopedia.tokopedianow.annotation.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.annotation.presentation.adapter.typefactory.BrandWidgetTypeFactory
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel

data class BrandWidgetUiModel(
    val id: String = "",
    val header: TokoNowDynamicHeaderUiModel = TokoNowDynamicHeaderUiModel(),
    val items: List<Visitable<*>> = emptyList(),
    val state: BrandWidgetState = BrandWidgetState.LOADING
) : Visitable<BrandWidgetTypeFactory>, ImpressHolder() {

    enum class BrandWidgetState {
        LOADING,
        LOADED,
        ERROR
    }

    override fun type(typeFactory: BrandWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
