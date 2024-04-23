package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel

interface TokoNowTitleTypeFactory {
    fun type(uiModel: TokoNowTitleUiModel): Int
}
