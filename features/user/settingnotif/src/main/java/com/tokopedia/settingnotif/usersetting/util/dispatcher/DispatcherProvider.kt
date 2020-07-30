package com.tokopedia.settingnotif.usersetting.util.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    fun trampoline(): CoroutineDispatcher
}