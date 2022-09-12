package com.tokopedia.dilayanitokopedia.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.adapter.typefactory.DtChooseAddressWidgetTypeFactory

data class DtChooseAddressWidgetUiModel(
        val id: String = ""
) : Visitable<DtChooseAddressWidgetTypeFactory>  {
    override fun type(typeFactory: DtChooseAddressWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}