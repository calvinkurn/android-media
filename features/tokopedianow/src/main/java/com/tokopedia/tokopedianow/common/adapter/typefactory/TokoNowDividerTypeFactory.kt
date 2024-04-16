package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel

interface TokoNowDividerTypeFactory {
    fun type(uiModel: TokoNowDividerUiModel): Int
}
