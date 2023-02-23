package com.tokopedia.play_common.shortsuploader.di.worker

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play_common.domain.UpdateChannelUseCase
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

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }
}
