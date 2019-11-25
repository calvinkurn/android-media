package com.tokopedia.topchat.chatsetting.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSettingViewModel @Inject constructor(
        private val mainDispatcher: CoroutineDispatcher,
        private val getChatSettingUseCase: GetChatSettingUseCase
) : BaseViewModel(mainDispatcher) {

    var isSeller = false

    private val _chatSettings = MutableLiveData<Result<List<ChatSetting>>>()
    val chatSettings: LiveData<Result<List<ChatSetting>>>
        get() = _chatSettings

    init {
        getChatSettingUseCase.get(
                onSuccessGetChatSetting(),
                onErrorGetChatSetting())
    }

    fun filterSettings(filter: (setting: ChatSetting) -> Boolean, chatSettings: List<ChatSetting>): List<ChatSetting> {
        return chatSettings.filter { setting ->
            filter(setting)
        }
    }

    fun initArguments(arguments: Bundle?) {
        if (arguments == null) return
        isSeller = arguments.getBoolean(TemplateChatActivity.PARAM_IS_SELLER, false)
    }

    private fun onSuccessGetChatSetting(): (GetChatSettingResponse) -> Unit {
        return { response ->
            val chatSettings = response.chatGetGearList.list
            _chatSettings.postValue(Success(chatSettings))
        }
    }

    private fun onErrorGetChatSetting(): (Throwable) -> Unit {
        return { error ->
            _chatSettings.postValue(Fail(error))
        }
    }

}