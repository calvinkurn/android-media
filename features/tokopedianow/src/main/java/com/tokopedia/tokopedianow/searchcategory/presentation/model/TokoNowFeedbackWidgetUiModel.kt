package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.TokoNowFeedbackWidgetTypeFactory

data class TokoNowFeedbackWidgetUiModel(
   var isDivider:Boolean = false
) : Visitable<TokoNowFeedbackWidgetTypeFactory> {
    override fun type(typeFactory: TokoNowFeedbackWidgetTypeFactory): Int = typeFactory.type(this)
}
