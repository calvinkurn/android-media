package com.tokopedia.brandlist.brandlist_category.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module(includes = [BrandlistCategoryViewModelModule::class])
class BrandlistCategoryModule {

    @BrandlistCategoryScope
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

}