package com.tokopedia.tokopedianow.searchcategory.presentation.typefactory

import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel

interface TokoNowFeedbackWidgetTypeFactory {
    fun type(uiModel: TokoNowFeedbackWidgetUiModel) : Int
}
