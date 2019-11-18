package com.tokopedia.topchat.chatsetting.viewmodel

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsetting.data.GetChatSettingResponse
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSettingViewModel @Inject constructor(
        private val mainDispatcher: CoroutineDispatcher,
        private val getChatSettingUseCase: GetChatSettingUseCase
) : BaseViewModel(mainDispatcher) {

    fun loadChatSettings() {
        getChatSettingUseCase.get(onSuccessGetChatSetting(), onErrorGetChatSetting())
    }

    private fun onSuccessGetChatSetting(): (GetChatSettingResponse) -> Unit {
        return { response ->
            Log.d("RESPONSE", "success")
        }
    }

    private fun onErrorGetChatSetting(): (Throwable) -> Unit {
        return { error ->
            Log.d("RESPONSE", "error")
        }
    }

}