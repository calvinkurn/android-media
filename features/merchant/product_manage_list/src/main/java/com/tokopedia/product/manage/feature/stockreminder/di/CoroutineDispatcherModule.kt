package com.tokopedia.product.manage.feature.stockreminder.di

import com.tokopedia.product.manage.feature.stockreminder.util.coroutine.CommonCoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcherModule(): CoroutineDispatcher = CommonCoroutineDispatcherProvider.io
}