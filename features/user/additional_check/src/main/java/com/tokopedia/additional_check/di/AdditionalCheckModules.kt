package com.tokopedia.additional_check.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.internal.TwoFactorTracker
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class AdditionalCheckModules {

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @ActivityScope
    @Provides
    fun providePreference(@ApplicationContext context: Context): AdditionalCheckPreference = AdditionalCheckPreference(context)

    @ActivityScope
    @Provides
    fun provideTracker(): TwoFactorTracker = TwoFactorTracker()
}