package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplySettingsUseCase
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplyTemplateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkSmartReplyDetailViewModel @Inject constructor(
        private val discussionSetSmartReplyTemplateUseCase: DiscussionSetSmartReplyTemplateUseCase,
        private val discussionSetSmartReplySettingsUseCase: DiscussionSetSmartReplySettingsUseCase,
        private val userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _setSmartReplyResult = MutableLiveData<Result<String>>()
    val setSmartReplyResult: LiveData<Result<String>>
        get() = _setSmartReplyResult

    var isSmartReplyOn: Boolean = false
    var messageReady: String = ""
    var messageNotReady: String = ""
    val shopName: String
        get() = userSession.shopName
    val shopAvatar: String
        get() = userSession.shopAvatar

    fun setSmartReply() {
        launchCatchError(block = {
            discussionSetSmartReplySettingsUseCase.setRequestParams(isSmartReplyOn)
            val response = discussionSetSmartReplySettingsUseCase.executeOnBackground()
            if (response.discussionSetSmartReplySetting.isSuccess) {
                _setSmartReplyResult.postValue(Success(response.discussionSetSmartReplySetting.reason))
            } else {
                _setSmartReplyResult.postValue(Fail(Throwable(message = response.discussionSetSmartReplySetting.reason)))
            }
        }) {
            _setSmartReplyResult.postValue(Fail(it))
        }
    }

    fun setSmartReplyTemplate() {
        launchCatchError(block = {
            discussionSetSmartReplyTemplateUseCase.setParams(messageReady, messageNotReady)
            val response = discussionSetSmartReplyTemplateUseCase.executeOnBackground()
            if (response.discussionSetSmartReplyTemplate.isSuccess) {
                _setSmartReplyResult.postValue(Success(response.discussionSetSmartReplyTemplate.reason))
            } else {
                _setSmartReplyResult.postValue(Fail(Throwable(message = response.discussionSetSmartReplyTemplate.reason)))
            }
        }) {
            _setSmartReplyResult.postValue(Fail(it))
        }
    }


}