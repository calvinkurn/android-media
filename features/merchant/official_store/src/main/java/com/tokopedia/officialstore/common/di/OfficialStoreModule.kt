package com.tokopedia.officialstore.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.officialstore.OfficialStoreDispatcherProvider
import com.tokopedia.officialstore.OfficialStoreDispatcherProviderImpl
import dagger.Module
import dagger.Provides

@Module
@OfficialStoreScope
class OfficialStoreModule {

    @OfficialStoreScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @OfficialStoreScope
    @Provides
    fun provideDispatcherProvider(): OfficialStoreDispatcherProvider = OfficialStoreDispatcherProviderImpl()

}
