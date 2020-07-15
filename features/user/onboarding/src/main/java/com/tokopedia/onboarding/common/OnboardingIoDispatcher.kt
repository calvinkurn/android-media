package com.tokopedia.onboarding.common

import kotlinx.coroutines.CoroutineDispatcher

interface OnboardingIoDispatcher {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}