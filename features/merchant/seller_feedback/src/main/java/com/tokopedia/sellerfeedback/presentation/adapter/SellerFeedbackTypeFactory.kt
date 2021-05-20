package com.tokopedia.sellerfeedback.presentation.adapter

import com.tokopedia.sellerfeedback.presentation.uimodel.SellerFeedbackFormUiModel

interface SellerFeedbackTypeFactory {

    fun type(uiModel: SellerFeedbackFormUiModel): Int
}
