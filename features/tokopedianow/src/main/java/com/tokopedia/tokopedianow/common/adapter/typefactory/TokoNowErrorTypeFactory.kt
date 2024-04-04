package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel

interface TokoNowErrorTypeFactory {
    fun type(uiModel: TokoNowErrorUiModel): Int
}
