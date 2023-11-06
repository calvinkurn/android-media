package com.tokopedia.recommendation_widget_common.widget.entitycard.typefactory

import com.tokopedia.recommendation_widget_common.widget.entitycard.model.RecomEntityCardUiModel

interface RecomEntityCardTypeFactory {
    fun type(uiModel: RecomEntityCardUiModel): Int
}
