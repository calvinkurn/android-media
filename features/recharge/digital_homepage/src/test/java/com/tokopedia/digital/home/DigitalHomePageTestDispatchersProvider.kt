package com.tokopedia.digital.home

import com.tokopedia.digital.home.presentation.Util.DigitalHomePageDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DigitalHomePageTestDispatchersProvider : DigitalHomePageDispatchersProvider() {
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
}