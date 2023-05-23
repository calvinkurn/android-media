package com.tokopedia.mediauploader.common.di

import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManagerImpl
import com.tokopedia.mediauploader.common.cache.TrackerCacheManager
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStore
import com.tokopedia.mediauploader.tracker.TrackerCacheDataStoreImpl
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepository
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepositoryImpl
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.VideoMetaDataExtractorImpl
import com.tokopedia.mediauploader.video.domain.GetChunkCheckerUseCase
import com.tokopedia.mediauploader.video.domain.GetChunkUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.InitVideoUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetAbortUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetCompleteUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import dagger.Binds
import dagger.Module

@Module(includes = [
    MediaUploaderNetworkModule::class,
    MediaUploaderCommonModule::class
])
abstract class MediaUploaderModule {

    // -- common --

    @Binds
    abstract fun provideSourcePolicyManager(
        impl: SourcePolicyManagerImpl
    ): SourcePolicyManager

    @Binds
    @MediaUploaderQualifier
    abstract fun provideTrackerCacheDataStore(
        impl: TrackerCacheDataStoreImpl
    ): TrackerCacheDataStore

    // -- image --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImagePolicyUseCase(
        impl: GetImagePolicyUseCase
    ): GetImagePolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImageUploaderUseCase(
        impl: GetImageUploaderUseCase
    ): GetImageUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImageSecurePolicyUseCase(
        impl: GetImageSecurePolicyUseCase
    ): GetImageSecurePolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideImageUploaderManager(
        impl: ImageUploaderManager
    ): ImageUploaderManager

    // -- common video --

    @Binds
    abstract fun provideVideoMetaDataExtractorRepository(
        impl: VideoMetaDataExtractorImpl
    ): VideoMetaDataExtractor

    @Binds
    abstract fun provideVideoCompressionRepository(
        impl: VideoCompressionRepositoryImpl
    ): VideoCompressionRepository

    @Binds
    @MediaUploaderQualifier
    abstract fun provideVideoCompressionCacheManager(
        impl: TrackerCacheManager
    ): TrackerCacheManager

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetVideoCompressionUseCase(
        impl: SetVideoCompressionUseCase
    ): SetVideoCompressionUseCase

    // -- simple video --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetVideoPolicyUseCase(
        impl: GetVideoPolicyUseCase
    ): GetVideoPolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetSimpleUploaderUseCase(
        impl: GetSimpleUploaderUseCase
    ): GetSimpleUploaderUseCase

    // -- large video --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideInitVideoUploaderUseCase(
        impl: InitVideoUploaderUseCase
    ): InitVideoUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetChunkCheckerUseCase(
        impl: GetChunkCheckerUseCase
    ): GetChunkCheckerUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetChunkUploaderUseCase(
        impl: GetChunkUploaderUseCase
    ): GetChunkUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetCompleteUploaderUseCase(
        impl: SetCompleteUploaderUseCase
    ): SetCompleteUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetAbortUploaderUseCase(
        impl: SetAbortUploaderUseCase
    ): SetAbortUploaderUseCase

}
