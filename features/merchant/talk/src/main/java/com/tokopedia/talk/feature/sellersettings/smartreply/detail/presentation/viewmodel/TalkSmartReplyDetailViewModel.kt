package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.DiscussionSmartReplyMutationResult
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplySettingsUseCase
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplyTemplateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkSmartReplyDetailViewModel @Inject constructor(
        private val discussionSetSmartReplyTemplateUseCase: DiscussionSetSmartReplyTemplateUseCase,
        private val discussionSetSmartReplySettingsUseCase: DiscussionSetSmartReplySettingsUseCase,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _setSmartReplyResult = MutableLiveData<com.tokopedia.usecase.coroutines.Result<DiscussionSmartReplyMutationResult>>()
    val setSmartReplyResult: LiveData<com.tokopedia.usecase.coroutines.Result<DiscussionSmartReplyMutationResult>>
        get() = _setSmartReplyResult

    private val _setSmartReplyTemplateResult = MutableLiveData<com.tokopedia.usecase.coroutines.Result<DiscussionSmartReplyMutationResult>>()
    val setSmartReplyTemplateResult: LiveData<com.tokopedia.usecase.coroutines.Result<DiscussionSmartReplyMutationResult>>
        get() = _setSmartReplyTemplateResult


    private var isSmartReplyOn: Boolean = false
    private var messageReady: String = ""
    private var messageNotReady: String = ""

    fun setIsSmartReplyOn(isSmartReplyOn: Boolean) {
        this.isSmartReplyOn = isSmartReplyOn
    }

    fun setMessageReady(messageReady: String) {
        this.messageReady = messageReady
    }

    fun setMessageNotReady(messageNotReady: String) {
        this.messageNotReady = messageNotReady
    }

    fun getIsSmartReplyOn(): Boolean {
        return isSmartReplyOn
    }

    fun getMessageReady(): String {
        return messageReady
    }

    fun getMessageNotReady(): String {
        return messageNotReady
    }

    fun setSmartReply() {
        launchCatchError(block = {
            discussionSetSmartReplySettingsUseCase.setRequestParams(isSmartReplyOn)
            val response = discussionSetSmartReplySettingsUseCase.executeOnBackground()
            _setSmartReplyResult.postValue(Success(response.discussionSetSmartReplySetting))
        }) {
            _setSmartReplyResult.postValue(Fail(it))
        }
    }
    fun setSmartReplyTemplate() {
        launchCatchError(block = {
            discussionSetSmartReplyTemplateUseCase.setParams(messageReady, messageNotReady)
            val response = discussionSetSmartReplyTemplateUseCase.executeOnBackground()
            _setSmartReplyResult.postValue(Success(response.discussionSetSmartReplyTemplate))
        }) {
            _setSmartReplyResult.postValue(Fail(it))
        }
    }


}