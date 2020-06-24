package com.tokopedia.review.feature.inbox.history.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel

class ReviewHistoryAdapter(
        reviewHistoryAdapterTypeFactory: ReviewHistoryAdapterTypeFactory
) : BaseListAdapter<ReviewHistoryUiModel, ReviewHistoryAdapterTypeFactory>(reviewHistoryAdapterTypeFactory)