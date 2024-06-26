package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.TalkSmartReplyDetailButtonState
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplySettingsUseCase
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase.DiscussionSetSmartReplyTemplateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class
TalkSmartReplyDetailViewModel @Inject constructor(
    private val discussionSetSmartReplyTemplateUseCase: DiscussionSetSmartReplyTemplateUseCase,
    private val discussionSetSmartReplySettingsUseCase: DiscussionSetSmartReplySettingsUseCase,
    private val userSession: UserSessionInterface,
    private val firebaseRemoteConfig: RemoteConfig,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _setSmartReplyResult = MutableLiveData<Result<String>>()
    val setSmartReplyResult: LiveData<Result<String>>
        get() = _setSmartReplyResult

    private val _buttonState = MutableLiveData<TalkSmartReplyDetailButtonState>(TalkSmartReplyDetailButtonState())
    val buttonState: LiveData<TalkSmartReplyDetailButtonState>
        get() = _buttonState

    private val _smartReplyDecommissionConfig: MediatorLiveData<SmartReplyTalkDecommissionConfig.SmartReplyStockPage> = MediatorLiveData()
    val smartReplyDecommissionConfig: LiveData<SmartReplyTalkDecommissionConfig.SmartReplyStockPage>
        get() = _smartReplyDecommissionConfig

    var isSmartReplyOn: Boolean = false
    var messageReady: String = ""
    var messageNotReady: String = ""
    private var originalMessageReady = ""
    private var originalMessageNotReady = ""
    val shopName: String
        get() = userSession.shopName
    val shopAvatar: String
        get() = userSession.shopAvatar

    init {
        getSmartReplyDecommissionConfig()
    }

    fun setSmartReply() {
        launchCatchError(block = {
            discussionSetSmartReplySettingsUseCase.setRequestParams(isSmartReplyOn)
            val response = discussionSetSmartReplySettingsUseCase.executeOnBackground()
            if (response.discussionSetSmartReplySetting.isSuccess) {
                _setSmartReplyResult.postValue(Success(response.discussionSetSmartReplySetting.reason))
            } else {
                _setSmartReplyResult.postValue(Fail(MessageErrorException(response.discussionSetSmartReplySetting.reason)))
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
                updateOriginalMessages(messageReady, messageNotReady)
            } else {
                _setSmartReplyResult.postValue(Fail(MessageErrorException(response.discussionSetSmartReplyTemplate.reason)))
            }
        }) {
            _setSmartReplyResult.postValue(Fail(it))
        }
    }

    fun initMessages() {
        originalMessageReady = messageReady
        originalMessageNotReady = messageNotReady
    }

    fun updateMessageChanged(message: String, isReady: Boolean) {
        if (isReady) {
            updateIsReadyTextChanged(message != originalMessageReady)
            return
        }
        updateIsNotReadyTextChanged(message != originalMessageNotReady)
    }

    private fun updateIsReadyTextChanged(isReadyTextChanged: Boolean) {
        _buttonState.value = _buttonState.value?.copy(isReadyTextChanged = isReadyTextChanged)
    }

    private fun updateIsNotReadyTextChanged(isNotReadyTextChanged: Boolean) {
        _buttonState.value = _buttonState.value?.copy(isNotReadyTextChanged = isNotReadyTextChanged)
    }

    fun updateIsSwitchActive(isSwitchActive: Boolean) {
        _buttonState.value = _buttonState.value?.copy(isSwitchActive = isSwitchActive)
    }

    private fun resetMessagesFromBackgroundThread() {
        _buttonState.postValue(_buttonState.value?.copy(isReadyTextChanged = false, isNotReadyTextChanged = false))
    }

    private fun updateOriginalMessages(messageReady: String, messageNotReady: String) {
        originalMessageReady = messageReady
        originalMessageNotReady = messageNotReady
        resetMessagesFromBackgroundThread()
    }

    private fun getSmartReplyDecommissionConfig() {
        viewModelScope.launchCatchError(block = {
            firebaseRemoteConfig.getString(
                TalkConstants.SMART_REPLY_DECOMMISSION_REMOTE_CONFIG_KEY
            ).let { jsonConfig ->
                val config = GsonSingleton
                    .instance
                    .fromJson(jsonConfig, SmartReplyTalkDecommissionConfig::class.java)
                    .smartReplyStockPage
                _smartReplyDecommissionConfig.postValue(config)
            }
        }, onError = {
                setDefaultSmartDecommissionConfig()
            })
    }

    private fun setDefaultSmartDecommissionConfig() {
        _smartReplyDecommissionConfig.postValue(SmartReplyTalkDecommissionConfig.SmartReplyStockPage())
    }
}
