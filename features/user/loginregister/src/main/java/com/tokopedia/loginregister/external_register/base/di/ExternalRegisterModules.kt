package com.tokopedia.loginregister.external_register.base.di

import android.content.Context
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
    @ExternalRegisterScope
    @ExternalRegisterContext
    fun provideContext(): Context = context

    @Provides
    @ExternalRegisterScope
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Main
}