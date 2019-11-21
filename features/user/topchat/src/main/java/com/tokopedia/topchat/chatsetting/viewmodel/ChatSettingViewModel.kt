package com.tokopedia.topchat.chatsetting.viewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.RouteManager
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

    fun loadChatSettings() {
        getChatSettingUseCase.get(onSuccessGetChatSetting(), onErrorGetChatSetting())
    }

    private fun onSuccessGetChatSetting(): (GetChatSettingResponse) -> Unit {
        return { response ->
            val chatSettings = response.chatGetGearList.list
            _chatSettings.postValue(Success(chatSettings))
        }
    }

    fun filterSettings(context: Context?, chatSettings: List<ChatSetting>): List<ChatSetting> {
        if (context == null) return chatSettings
        return chatSettings.filter { setting ->
            RouteManager.isSupportApplink(context, setting.link)
        }
    }

    private fun onErrorGetChatSetting(): (Throwable) -> Unit {
        return { error ->
            _chatSettings.postValue(Fail(error))
        }
    }

    fun initArguments(arguments: Bundle?) {
        if (arguments == null) return
         isSeller = arguments.getBoolean(TemplateChatActivity.PARAM_IS_SELLER, false)
    }

}