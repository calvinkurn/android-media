package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel

interface TokoNowHeaderTypeFactory {
    fun type(uiModel: TokoNowHeaderUiModel): Int
}
