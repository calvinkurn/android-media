package com.tokopedia.onboarding.di.module

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.data.OnboardingConstant
import com.tokopedia.onboarding.di.OnboardingScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class DynamicOnboardingQueryModule(private val activity: Activity) {

    @OnboardingScope
    @Provides
    fun getContext(): Context = activity

    @OnboardingScope
    @Provides
    @IntoMap
    @StringKey(OnboardingConstant.Query.QUERY_DYNAMIC_ONBAORDING)
    fun provideDynamicOnbaordingQuery(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_dynamic_onbaording)
    }
}