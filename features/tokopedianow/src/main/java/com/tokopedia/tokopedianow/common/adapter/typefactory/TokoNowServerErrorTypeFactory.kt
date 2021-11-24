package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel

interface TokoNowServerErrorTypeFactory {
    fun type(uiModel: TokoNowServerErrorUiModel): Int
}