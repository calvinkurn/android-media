package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.data.DiscussionGetSmartReply
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.domain.usecase.DiscussionGetSmartReplyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkSmartReplySettingsViewModel @Inject constructor(
        private val discussionGetSmartReplyUseCase: DiscussionGetSmartReplyUseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _smartReplyData = MutableLiveData<Result<DiscussionGetSmartReply>>()
    val smartReplyData: LiveData<Result<DiscussionGetSmartReply>>
        get() = _smartReplyData

    fun getSmartReplyData() {
        launchCatchError(block = {
            val response = discussionGetSmartReplyUseCase.executeOnBackground()
            _smartReplyData.postValue(Success(response.discussionGetSmartReply))
        }) {
            _smartReplyData.postValue(Fail(it))
        }
    }
}