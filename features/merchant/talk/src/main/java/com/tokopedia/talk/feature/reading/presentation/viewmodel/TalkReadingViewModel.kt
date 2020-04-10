package com.tokopedia.talk.feature.reading.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.talk.feature.reading.domain.usecase.GetDiscussionAggregateUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TalkReadingViewModel @Inject constructor(
        private val getDiscussionAggregateUseCase: GetDiscussionAggregateUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun getDiscussionAggregate(productId: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
            }
        }) {

        }
    }

}