package com.tokopedia.topchat.chatsetting.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSettingViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

}