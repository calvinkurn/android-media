package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.homenav.mainnav.data.factory.MainNavFactory
import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import dagger.Module
import dagger.Provides

@Module
class MainNavMapperModule {

    @MainNavScope
    @Provides
    fun provideMainNavMapper(@ApplicationContext context: Context, mainNavFactory: MainNavFactory)
            = MainNavMapper(context, mainNavFactory)
}