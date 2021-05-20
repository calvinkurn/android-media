package com.tokopedia.sellerfeedback.presentation.adapter

import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel

interface FeedbackPageTypeFactory {
    fun type(uiModel: FeedbackPageUiModel): Int
}