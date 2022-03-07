package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateOocUiModel

interface TokoNowEmptyStateOocTypeFactory {
    fun type(uiModel: TokoNowEmptyStateOocUiModel): Int
}