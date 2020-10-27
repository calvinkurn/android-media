package com.tokopedia.topchat.chatsetting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
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

    private val _chatSettings = MutableLiveData<Result<List<Visitable<ChatSettingTypeFactory>>>>()
    val chatSettings: LiveData<Result<List<Visitable<ChatSettingTypeFactory>>>>
        get() = _chatSettings

    fun getChatSetting() {
        getChatSettingUseCase.get(onSuccessGetChatSetting(), onErrorGetChatSetting())
    }

    private fun onSuccessGetChatSetting(): (List<Visitable<ChatSettingTypeFactory>>) -> Unit {
        return { response ->
            _chatSettings.postValue(Success(response))
        }
    }

    private fun onErrorGetChatSetting(): (Throwable) -> Unit {
        return { error ->
            _chatSettings.postValue(Fail(error))
        }
    }

}