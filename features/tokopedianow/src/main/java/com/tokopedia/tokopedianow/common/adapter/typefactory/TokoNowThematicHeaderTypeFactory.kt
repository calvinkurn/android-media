package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel

interface TokoNowThematicHeaderTypeFactory {
    fun type(uiModel: TokoNowThematicHeaderUiModel): Int
}
