package com.tokopedia.brandlist.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class BrandlistModule  {

    @BrandlistScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @BrandlistScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}