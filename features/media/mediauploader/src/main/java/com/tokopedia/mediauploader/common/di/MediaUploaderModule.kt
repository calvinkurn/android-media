package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.VideoMetaDataExtractorImpl
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManagerImpl
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.data.store.base.CacheDataStoreImpl
import com.tokopedia.mediauploader.common.data.store.util.Serializer
import com.tokopedia.mediauploader.data.repository.LogRepository
import com.tokopedia.mediauploader.data.repository.LogRepositoryImpl
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.analytics.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.analytics.datastore.AnalyticsCacheDataStoreImpl
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepository
import com.tokopedia.mediauploader.video.data.repository.VideoCompressionRepositoryImpl
import com.tokopedia.mediauploader.video.domain.GetChunkCheckerUseCase
import com.tokopedia.mediauploader.video.domain.GetChunkUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.InitVideoUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetAbortUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetCompleteUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetVideoCompressionUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [MediaUploaderNetworkModule::class])
abstract class MediaUploaderModule {

    // -- common --

    @Binds
    abstract fun provideLogRepository(impl: LogRepositoryImpl): LogRepository

    @Binds
    abstract fun provideSourcePolicyManager(impl: SourcePolicyManagerImpl): SourcePolicyManager

    @Binds
    abstract fun provideVideoMetaDataExtractor(impl: VideoMetaDataExtractorImpl): VideoMetaDataExtractor

    @Binds
    abstract fun provideVideoCompressionRepository(impl: VideoCompressionRepositoryImpl): VideoCompressionRepository

    // -- image --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImagePolicyUseCase(impl: GetImagePolicyUseCase): GetImagePolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImageUploaderUseCase(impl: GetImageUploaderUseCase): GetImageUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetImageSecurePolicyUseCase(impl: GetImageSecurePolicyUseCase): GetImageSecurePolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideImageUploaderManager(impl: ImageUploaderManager): ImageUploaderManager

    // -- common video --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetVideoCompressionUseCase(impl: SetVideoCompressionUseCase): SetVideoCompressionUseCase

    // -- simple video --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetVideoPolicyUseCase(impl: GetVideoPolicyUseCase): GetVideoPolicyUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetSimpleUploaderUseCase(impl: GetSimpleUploaderUseCase): GetSimpleUploaderUseCase

    // -- large video --

    @Binds
    @MediaUploaderQualifier
    abstract fun provideInitVideoUploaderUseCase(impl: InitVideoUploaderUseCase): InitVideoUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetChunkCheckerUseCase(impl: GetChunkCheckerUseCase): GetChunkCheckerUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideGetChunkUploaderUseCase(impl: GetChunkUploaderUseCase): GetChunkUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetCompleteUploaderUseCase(impl: SetCompleteUploaderUseCase): SetCompleteUploaderUseCase

    @Binds
    @MediaUploaderQualifier
    abstract fun provideSetAbortUploaderUseCase(impl: SetAbortUploaderUseCase): SetAbortUploaderUseCase

    @Module
    companion object {

        @Provides
        @MediaUploaderQualifier
        fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
            return UserSession(context)
        }

        @Provides
        fun provideTrackerCacheDataStore(
            @ApplicationContext context: Context,
            metaDataExtractor: VideoMetaDataExtractor
        ): AnalyticsCacheDataStore {
            return AnalyticsCacheDataStoreImpl(
                metaDataExtractor,
                object : CacheDataStoreImpl<UploaderTracker>(context) {
                    override fun default(cache: UploaderTracker.() -> Unit) = UploaderTracker().apply(cache)
                    override fun read(string: String) = Serializer.read<UploaderTracker>(string)
                    override fun write(data: UploaderTracker) = Serializer.write(data)
                }
            )
        }
    }
}
