package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowProgressBarUiModel

interface TokoNowProgressBarTypeFactory {
    fun type(uiModel: TokoNowProgressBarUiModel): Int
}
