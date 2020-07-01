package com.tokopedia.brandlist.brandlist_category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchScope
import com.tokopedia.brandlist.common.BrandlistDispatcherProvider
import com.tokopedia.brandlist.common.BrandlistDispatcherProviderImp
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module(includes = [BrandlistCategoryViewModelModule::class])
@BrandlistCategoryScope
class BrandlistCategoryModule {

    @BrandlistCategoryScope
    @Provides
    fun provideDispatcherProvider(): BrandlistDispatcherProvider = BrandlistDispatcherProviderImp()

}