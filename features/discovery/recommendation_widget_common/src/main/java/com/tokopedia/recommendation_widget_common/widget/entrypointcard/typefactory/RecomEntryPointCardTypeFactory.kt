package com.tokopedia.recommendation_widget_common.widget.entrypointcard.typefactory

import com.tokopedia.recommendation_widget_common.widget.entrypointcard.model.RecomEntryPointCardUiModel

interface RecomEntryPointCardTypeFactory {
    fun type(uiModel: RecomEntryPointCardUiModel): Int
}
