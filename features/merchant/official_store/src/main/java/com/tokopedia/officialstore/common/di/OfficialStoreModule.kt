package com.tokopedia.officialstore.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@Module
class OfficialStoreModule {

    @OfficialStoreScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
}
