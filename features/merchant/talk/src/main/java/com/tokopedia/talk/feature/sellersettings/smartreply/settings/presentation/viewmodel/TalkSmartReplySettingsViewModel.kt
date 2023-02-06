package com.tokopedia.talk.feature.sellersettings.smartreply.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import com.tokopedia.talk.feature.inbox.data.TickerConfig
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.data.DiscussionGetSmartReply
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.domain.usecase.DiscussionGetSmartReplyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkSmartReplySettingsViewModel @Inject constructor(
        private val discussionGetSmartReplyUseCase: DiscussionGetSmartReplyUseCase,
        private val firebaseRemoteConfig: RemoteConfig,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _smartReplyData = MutableLiveData<Result<DiscussionGetSmartReply>>()
    val smartReplyData: LiveData<Result<DiscussionGetSmartReply>>
        get() = _smartReplyData

    private val _smartReplyDecommissionConfig: MutableLiveData<SmartReplyTalkDecommissionConfig.SmartReplyPage> = MutableLiveData()
    val smartReplyDecommissionConfig: LiveData<SmartReplyTalkDecommissionConfig.SmartReplyPage>
        get() = _smartReplyDecommissionConfig

    init {
        getSmartReplyDecommissionConfig()
    }

    fun getSmartReplyData() {
        launchCatchError(block = {
            val response = discussionGetSmartReplyUseCase.executeOnBackground()
            _smartReplyData.postValue(Success(response.discussionGetSmartReply))
        }) {
            _smartReplyData.postValue(Fail(it))
        }
    }

    private fun getSmartReplyDecommissionConfig() {
        viewModelScope.launchCatchError(block = {
            firebaseRemoteConfig.getString(
                TalkConstants.SMART_REPLY_DECOMMISSION_REMOTE_CONFIG_KEY
            ).let { jsonConfig ->
                val config = GsonSingleton
                    .instance
                    .fromJson(jsonConfig, SmartReplyTalkDecommissionConfig::class.java)
                    .smartReplyPage
                _smartReplyDecommissionConfig.postValue(config)
            }
        }, onError = {
            setDefaultSmartDecommissionConfig()
        })
    }

    private fun setDefaultSmartDecommissionConfig() {
        _smartReplyDecommissionConfig.postValue(
            SmartReplyTalkDecommissionConfig.SmartReplyPage(
                TickerConfig(show = false, title = "", text = "")
            )
        )
    }
}
