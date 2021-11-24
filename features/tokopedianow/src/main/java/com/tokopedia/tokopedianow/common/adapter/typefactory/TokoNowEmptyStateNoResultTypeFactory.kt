package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel

interface TokoNowEmptyStateNoResultTypeFactory {
    fun type(uiModel: TokoNowEmptyStateNoResultUiModel): Int
}