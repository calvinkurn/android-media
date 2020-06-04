package com.tokopedia.review.feature.inbox.pending.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.review.feature.inbox.pending.domain.usecase.ProductrevWaitForFeedbackUseCase
import javax.inject.Inject

class ReviewPendingViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val productrevWaitForFeedbackUseCase: ProductrevWaitForFeedbackUseCase
) : BaseViewModel(dispatchers.io) {


}