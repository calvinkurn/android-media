package com.tokopedia.review.feature.inbox.history.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.review.feature.inbox.history.domain.usecase.ProductrevFeedbackHistoryUseCase
import javax.inject.Inject

class ReviewHistoryViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val productrevFeedbackHistoryUseCase: ProductrevFeedbackHistoryUseCase
) : BaseViewModel(dispatchers.io) {
}