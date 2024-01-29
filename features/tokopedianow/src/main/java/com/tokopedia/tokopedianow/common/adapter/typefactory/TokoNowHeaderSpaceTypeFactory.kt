package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel

interface TokoNowHeaderSpaceTypeFactory {
    fun type(uiModel: TokoNowHeaderSpaceUiModel): Int
}
