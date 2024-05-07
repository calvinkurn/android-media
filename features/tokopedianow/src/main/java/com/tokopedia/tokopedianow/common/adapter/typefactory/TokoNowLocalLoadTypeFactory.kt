package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowLocalLoadUiModel

interface TokoNowLocalLoadTypeFactory {
    fun type(uiModel: TokoNowLocalLoadUiModel): Int
}
