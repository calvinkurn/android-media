package com.tokopedia.additional_check.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class AdditionalCheckModules {

    @AdditionalCheckScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @AdditionalCheckScope
    @Provides
    fun providePreference(@ApplicationContext context: Context): AdditionalCheckPreference = AdditionalCheckPreference(context)

}