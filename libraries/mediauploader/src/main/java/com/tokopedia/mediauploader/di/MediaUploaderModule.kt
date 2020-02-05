package com.tokopedia.mediauploader.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.consts.MediaUploaderQuery
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase
import dagger.Module
import dagger.Provides

@Module class MediaUploaderModule {

    @MediaUploaderQualifier
    @Provides
    fun provideDataPolicyUseCase(): DataPolicyUseCase {
        return DataPolicyUseCase(
                MediaUploaderQuery.dataPolicyQuery,
                GraphqlInteractor.getInstance().graphqlRepository
        )
    }

    @MediaUploaderQualifier
    @Provides
    fun provideMediaUploaderUseCase(
            @MediaUploaderQualifier services: UploaderServices
    ): MediaUploaderUseCase {
        return MediaUploaderUseCase(services)
    }

}