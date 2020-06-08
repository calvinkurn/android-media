package com.tokopedia.settingnotif.usersetting.util.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun ui(): CoroutineDispatcher = Dispatchers.Unconfined
    override fun trampoline(): CoroutineDispatcher = Dispatchers.Unconfined
}