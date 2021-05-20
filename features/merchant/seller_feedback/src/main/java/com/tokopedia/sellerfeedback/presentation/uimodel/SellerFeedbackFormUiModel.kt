package com.tokopedia.sellerfeedback.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerfeedback.presentation.adapter.SellerFeedbackTypeFactory

object SellerFeedbackFormUiModel : Visitable<SellerFeedbackTypeFactory> {
    override fun type(typeFactory: SellerFeedbackTypeFactory): Int {
        return typeFactory.type(this)
    }
}
