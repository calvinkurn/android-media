package com.tokopedia.talk.feature.sellersettings.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.SmartReplyTalkDecommissionConfig
import javax.inject.Inject

class TalkSettingsViewModel @Inject constructor(
    private val firebaseRemoteConfig: RemoteConfig
) : ViewModel() {

    private val _smartReplyDecommissionConfig: MutableLiveData<SmartReplyTalkDecommissionConfig.TalkSettingPage> = MutableLiveData()
    val smartReplyDecommissionConfig: LiveData<SmartReplyTalkDecommissionConfig.TalkSettingPage>
        get() = _smartReplyDecommissionConfig

    init {
        getSmartReplyDecommissionConfig()
    }

    private fun getSmartReplyDecommissionConfig() {
        viewModelScope.launchCatchError(block = {
            firebaseRemoteConfig.getString(
                TalkConstants.SMART_REPLY_DECOMMISSION_REMOTE_CONFIG_KEY
            ).let { jsonConfig ->
                val config = GsonSingleton
                    .instance
                    .fromJson(jsonConfig, SmartReplyTalkDecommissionConfig::class.java)
                    .talkSettingPage
                _smartReplyDecommissionConfig.postValue(config)
            }
        }, onError = {
                setDefaultSmartDecommissionConfig()
            })
    }

    private fun setDefaultSmartDecommissionConfig() {
        _smartReplyDecommissionConfig.postValue(SmartReplyTalkDecommissionConfig.TalkSettingPage())
    }
}
