package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowSectionHeaderUiModel

interface TokoNowSectionHeaderTypeFactory {

    fun type(uiModel: TokoNowSectionHeaderUiModel): Int
}