package com.tokopedia.sellerfeedback.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerfeedback.presentation.adapter.FeedbackPageTypeFactory

data class FeedbackPageUiModel(
        val title:String
) : Visitable<FeedbackPageTypeFactory> {
    override fun type(typeFactory: FeedbackPageTypeFactory): Int {
        return typeFactory.type(this)
    }
}