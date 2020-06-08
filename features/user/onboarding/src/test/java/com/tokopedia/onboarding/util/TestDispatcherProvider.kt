package com.tokopedia.onboarding.util

import com.tokopedia.onboarding.common.OnboardingIoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: OnboardingIoDispatcher {

    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}