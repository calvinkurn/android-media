package com.tokopedia.mediauploader.video.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.MediaUploaderQualifier
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.domain.GetSingleVideoUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import dagger.Module
import dagger.Provides

@Module class VideoUploaderModule {

    @Provides
    @MediaUploaderQualifier
    fun provideGetVideoPolicyUseCase(
        repository: GraphqlRepository
    ): GetVideoPolicyUseCase {
        return GetVideoPolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetSingleVideoUploaderUseCase(
        services: VideoUploadServices
    ): GetSingleVideoUploaderUseCase {
        return GetSingleVideoUploaderUseCase(services)
    }

}