package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel

interface TokoNowChipTypeFactory {

    fun type(uiModel: TokoNowChipUiModel): Int
}