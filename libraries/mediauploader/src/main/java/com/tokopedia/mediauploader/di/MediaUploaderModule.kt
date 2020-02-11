package com.tokopedia.mediauploader.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase
import com.tokopedia.mediauploader.domain.UploaderUseCase
import dagger.Module
import dagger.Provides

@Module class MediaUploaderModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @MediaUploaderQualifier
    fun provideDataPolicyUseCase(
            graphqlRepository: GraphqlRepository
    ): DataPolicyUseCase {
        return DataPolicyUseCase(graphqlRepository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderUseCase(
            services: UploaderServices
    ): MediaUploaderUseCase {
        return MediaUploaderUseCase(services)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideUploaderUseCase(
            dataPolicyUseCase: DataPolicyUseCase,
            mediaUploaderUseCase: MediaUploaderUseCase
    ): UploaderUseCase {
        return UploaderUseCase(dataPolicyUseCase, mediaUploaderUseCase)
    }

}