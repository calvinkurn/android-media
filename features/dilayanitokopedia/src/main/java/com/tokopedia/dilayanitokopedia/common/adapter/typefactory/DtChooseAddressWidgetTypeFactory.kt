package com.tokopedia.dilayanitokopedia.common.adapter.typefactory

import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel


interface DtChooseAddressWidgetTypeFactory {
    fun type(uiModel: DtChooseAddressWidgetUiModel): Int
}