package com.tokopedia.additional_check.di

import android.content.Context
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class AdditionalCheckModules(val context: Context) {

    @AdditionalCheckScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @AdditionalCheckContext
    fun provideContext(): Context = context

    @AdditionalCheckScope
    @Provides
    fun providePreference(@AdditionalCheckContext context: Context): AdditionalCheckPreference = AdditionalCheckPreference(context)

}