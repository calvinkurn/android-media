package com.tokopedia.topchat

import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TopchatAndroidTestCoroutineContextDispatcher @Inject constructor(

) : TopchatCoroutineContextProvider() {
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
}