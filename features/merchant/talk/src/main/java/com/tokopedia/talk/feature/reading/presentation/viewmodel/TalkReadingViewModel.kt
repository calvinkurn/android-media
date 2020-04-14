package com.tokopedia.talk.feature.reading.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class TalkReadingViewModel @Inject constructor(
        private val getDiscussionAggregateUseCase: GetDiscussionAggregateUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _discussionAggregate = MutableLiveData<Result<DiscussionAggregate>>()
    val discussionAggregate: LiveData<Result<DiscussionAggregate>>
    get() = _discussionAggregate

    fun getDiscussionAggregate(productId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getDiscussionAggregateUseCase.setParams(productId.toIntOrZero())
                getDiscussionAggregateUseCase.executeOnBackground()
            }
            _discussionAggregate.postValue(Success(response))
        }) {
            _discussionAggregate.postValue(Fail(it))
        }
    }

}