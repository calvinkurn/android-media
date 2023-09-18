package com.tokopedia.creation.common.upload.di.worker

import com.tokopedia.creation.common.upload.di.CreationUploadDataModule
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
@Module(
    includes = [CreationUploadDataModule::class]
)
class CreationUploadWorkerModule {

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }
}
