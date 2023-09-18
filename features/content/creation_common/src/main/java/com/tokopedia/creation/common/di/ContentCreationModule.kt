package com.tokopedia.creation.common.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.domain.ContentCreationConfigUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * Created By : Muhammad Furqan on 15/09/23
 */
@Module(includes = [ContentCreationViewModelModule::class])
class ContentCreationModule {
    @Provides
    fun provideContentCreationConfigUseCase(
        graphqlRepository: GraphqlRepository,
        dispatchers: CoroutineDispatchers
    ): ContentCreationConfigUseCase = ContentCreationConfigUseCase(
        graphqlRepository = graphqlRepository,
        dispatcher = dispatchers
    )
}
