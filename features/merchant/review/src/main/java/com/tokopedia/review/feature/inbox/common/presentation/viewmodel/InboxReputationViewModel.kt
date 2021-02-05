package com.tokopedia.review.feature.inbox.common.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.review.feature.inbox.common.domain.usecase.InboxReviewCounterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InboxReputationViewModel @Inject constructor(
        private val inboxReviewCounterUseCase: InboxReviewCounterUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _inboxReviewCounterText = MutableLiveData<Result<String>>()
    val inboxReviewCounterText: LiveData<Result<String>>
        get() = _inboxReviewCounterText

    fun getInboxReviewCounter() {
        launchCatchError(block = {
            val counterResult = withContext(dispatchers.io) {
                inboxReviewCounterUseCase.executeOnBackground().productrevReviewTabCounter.list.firstOrNull()?.count.toString()
            }
            _inboxReviewCounterText.postValue(Success(counterResult))
        }, onError = {
            _inboxReviewCounterText.postValue(Fail(it))
        })
    }

}