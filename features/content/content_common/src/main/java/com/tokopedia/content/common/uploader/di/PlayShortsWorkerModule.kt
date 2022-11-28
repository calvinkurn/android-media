package com.tokopedia.content.common.uploader.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */
@Module
internal class PlayShortsWorkerModule {

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

}
