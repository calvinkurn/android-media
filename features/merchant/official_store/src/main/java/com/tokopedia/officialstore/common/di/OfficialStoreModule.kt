package com.tokopedia.officialstore.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@OfficialStoreScope
class OfficialStoreModule {

    @OfficialStoreScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @OfficialStoreScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
