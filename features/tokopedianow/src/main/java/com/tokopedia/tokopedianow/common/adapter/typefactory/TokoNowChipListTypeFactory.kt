package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel

interface TokoNowChipListTypeFactory {

    fun type(uiModel: TokoNowChipListUiModel): Int
}