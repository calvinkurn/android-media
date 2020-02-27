package com.tokopedia.product.manage.stock_reminder.di

import com.tokopedia.product.manage.stock_reminder.util.coroutine.CommonCoroutineDispatcherProvider
import com.tokopedia.product.manage.stock_reminder.util.coroutine.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcherModule(): CoroutineDispatcher = CommonCoroutineDispatcherProvider.io
}