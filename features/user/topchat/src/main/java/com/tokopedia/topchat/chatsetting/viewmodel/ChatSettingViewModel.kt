package com.tokopedia.topchat.chatsetting.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSettingViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val getChatSettingUseCase: GetChatSettingUseCase
) : BaseViewModel(dispatcher) {

    fun loadChatSettings() {

    }

}