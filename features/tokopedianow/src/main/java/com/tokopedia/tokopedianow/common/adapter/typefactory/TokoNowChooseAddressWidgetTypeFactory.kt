package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

interface TokoNowChooseAddressWidgetTypeFactory {
    fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int
}