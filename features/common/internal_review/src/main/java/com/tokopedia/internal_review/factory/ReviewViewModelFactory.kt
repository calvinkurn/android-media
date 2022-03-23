package com.tokopedia.internal_review.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.internal_review.domain.usecase.SendReviewUseCase
import com.tokopedia.internal_review.view.viewmodel.ReviewViewModel


class ReviewViewModelFactory constructor(val reviewUseCase: SendReviewUseCase,
                                         val dispatchers: CoroutineDispatchers) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReviewViewModel(reviewUseCase, dispatchers) as T
    }
}