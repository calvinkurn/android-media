package com.tokopedia.officialstore.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
class OfficialStoreModule {

    @OfficialStoreScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @OfficialStoreScope
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

}
