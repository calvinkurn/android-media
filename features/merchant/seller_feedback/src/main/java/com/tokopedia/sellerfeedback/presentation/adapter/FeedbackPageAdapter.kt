package com.tokopedia.sellerfeedback.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerfeedback.presentation.uimodel.FeedbackPageUiModel

class FeedbackPageAdapter(
        factory: FeedbackPageAdapterFactory
) : BaseListAdapter<FeedbackPageUiModel, FeedbackPageAdapterFactory>(factory)