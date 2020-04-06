package com.tokopedia.onboarding.common

import kotlinx.coroutines.CoroutineDispatcher

interface OnboardingIoDispatcher {
    val main: CoroutineDispatcher
}