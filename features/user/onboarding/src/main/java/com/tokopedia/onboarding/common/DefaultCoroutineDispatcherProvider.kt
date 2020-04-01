package com.tokopedia.onboarding.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultCoroutineDispatcherProvider : OnboardingIoDispatcher {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
}