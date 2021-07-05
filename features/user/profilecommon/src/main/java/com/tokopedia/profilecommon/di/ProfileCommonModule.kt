package com.tokopedia.profilecommon.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

@Module(includes = [
    ProfileCommonQueryModule::class,
    ProfileCommonUseCaseModule::class
])
class ProfileCommonModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ProfileCommonScope
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}