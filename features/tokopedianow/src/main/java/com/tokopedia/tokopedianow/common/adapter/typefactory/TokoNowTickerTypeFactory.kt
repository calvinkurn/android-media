package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel

interface TokoNowTickerTypeFactory {
    fun type(uiModel: TokoNowTickerUiModel): Int
}
