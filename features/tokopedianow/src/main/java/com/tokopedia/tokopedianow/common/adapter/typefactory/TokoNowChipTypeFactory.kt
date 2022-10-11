package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowNotChipUiModel

interface TokoNowChipTypeFactory {

    fun type(uiModel: TokoNowChipUiModel): Int
    fun type(uiModel: TokoNowNotChipUiModel): Int
}