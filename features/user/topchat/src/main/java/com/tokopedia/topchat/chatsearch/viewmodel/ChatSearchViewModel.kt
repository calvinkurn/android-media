package com.tokopedia.topchat.chatsearch.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {
}