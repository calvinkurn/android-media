package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel

interface TokoNowBundleTypeFactory {
    fun type(uiModel: TokoNowBundleUiModel): Int
}
