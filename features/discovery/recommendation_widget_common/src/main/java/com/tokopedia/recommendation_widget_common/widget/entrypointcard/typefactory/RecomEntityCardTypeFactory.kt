package com.tokopedia.recommendation_widget_common.widget.entrypointcard.typefactory

import com.tokopedia.recommendation_widget_common.widget.entrypointcard.model.RecomEntityCardUiModel

interface RecomEntityCardTypeFactory {
    fun type(uiModel: RecomEntityCardUiModel): Int
}
