package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowFeedbackWidgetUiModel

interface TokoNowFeedbackWidgetTypeFactory {
    fun type(uiModel:TokoNowFeedbackWidgetUiModel) : Int
}
