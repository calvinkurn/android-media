package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory

data class TokoNowChooseAddressWidgetUiModel(
        val id: String = ""
) : Visitable<TokoNowChooseAddressWidgetTypeFactory>  {
    override fun type(typeFactory: TokoNowChooseAddressWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}