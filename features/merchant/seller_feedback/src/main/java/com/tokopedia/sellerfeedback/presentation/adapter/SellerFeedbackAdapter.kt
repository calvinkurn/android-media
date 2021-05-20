package com.tokopedia.sellerfeedback.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerfeedback.presentation.uimodel.SellerFeedbackFormUiModel

class SellerFeedbackAdapter(
    factory: SellerFeedbackAdapterFactory
): BaseListAdapter<SellerFeedbackFormUiModel, SellerFeedbackAdapterFactory>(factory)