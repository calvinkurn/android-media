package com.tokopedia.onboarding.di.module

import android.app.Activity
import android.content.Context
import com.tokopedia.onboarding.di.OnboardingScope
import dagger.Module
import dagger.Provides

@Module
class DynamicOnboardingQueryModule(private val activity: Activity) {

    @OnboardingScope
    @Provides
    fun getContext(): Context = activity
}
