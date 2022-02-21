package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel

interface TokoNowRepurchaseTypeFactory {
    fun type(uiModel: TokoNowRepurchaseUiModel): Int
}