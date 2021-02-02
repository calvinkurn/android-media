package com.tokopedia.loginregister.external_register.base.di

import android.content.Context
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
class ExternalRegisterModules(val context: Context) {
    @Provides
    @ExternalRegisterContext
    fun provideContext(): Context = context

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideExternalRegisterPreference(@ExternalRegisterContext context: Context): ExternalRegisterPreference {
        return ExternalRegisterPreference(context)
    }
}