package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.feature.inboxreview.domain.usecase.GetInboxReviewUseCase
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class InboxReviewViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getInboxReviewUseCase: GetInboxReviewUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    private val _inboxReview = MutableLiveData<Result<InboxReviewViewModel>>()
    val inboxReview: LiveData<Result<InboxReviewViewModel>>
        get() = _inboxReview




}