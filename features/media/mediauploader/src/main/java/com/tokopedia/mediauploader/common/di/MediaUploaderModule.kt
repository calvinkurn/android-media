package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStoreImpl
import com.tokopedia.mediauploader.video.internal.VideoCompressor
import com.tokopedia.mediauploader.video.internal.VideoCompressorImpl
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractor
import com.tokopedia.mediauploader.video.internal.VideoMetaDataExtractorImpl
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.cache.SourcePolicyManagerImpl
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.data.store.base.CacheDataStoreImpl
import com.tokopedia.mediauploader.common.data.store.util.Serializer
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
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
    @UploaderQualifier
    abstract fun provideVideoCompressor(impl: VideoCompressorImpl): VideoCompressor

    @Binds
    @UploaderQualifier
    abstract fun provideSourcePolicyManager(impl: SourcePolicyManagerImpl): SourcePolicyManager

    @Binds
    @UploaderQualifier
    abstract fun provideVideoMetaDataExtractor(impl: VideoMetaDataExtractorImpl): VideoMetaDataExtractor

    @Binds
    @UploaderQualifier
    abstract fun provideVideoCompressionRepository(impl: VideoCompressionRepositoryImpl): VideoCompressionRepository

    // -- image --

    @Binds
    @UploaderQualifier
    abstract fun provideGetImagePolicyUseCase(impl: GetImagePolicyUseCase): GetImagePolicyUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideGetImageUploaderUseCase(impl: GetImageUploaderUseCase): GetImageUploaderUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideGetImageSecurePolicyUseCase(impl: GetImageSecurePolicyUseCase): GetImageSecurePolicyUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideImageUploaderManager(impl: ImageUploaderManager): ImageUploaderManager

    // -- common video --

    @Binds
    @UploaderQualifier
    abstract fun provideSetVideoCompressionUseCase(impl: SetVideoCompressionUseCase): SetVideoCompressionUseCase

    // -- simple video --

    @Binds
    @UploaderQualifier
    abstract fun provideGetVideoPolicyUseCase(impl: GetVideoPolicyUseCase): GetVideoPolicyUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideGetSimpleUploaderUseCase(impl: GetSimpleUploaderUseCase): GetSimpleUploaderUseCase

    // -- large video --

    @Binds
    @UploaderQualifier
    abstract fun provideInitVideoUploaderUseCase(impl: InitVideoUploaderUseCase): InitVideoUploaderUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideGetChunkCheckerUseCase(impl: GetChunkCheckerUseCase): GetChunkCheckerUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideGetChunkUploaderUseCase(impl: GetChunkUploaderUseCase): GetChunkUploaderUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideSetCompleteUploaderUseCase(impl: SetCompleteUploaderUseCase): SetCompleteUploaderUseCase

    @Binds
    @UploaderQualifier
    abstract fun provideSetAbortUploaderUseCase(impl: SetAbortUploaderUseCase): SetAbortUploaderUseCase

    @Module
    companion object {

        @Provides
        @UploaderQualifier
        fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
            return UserSession(context)
        }

        @Provides
        @UploaderQualifier
        fun provideTrackerCacheDataStore(
            @ApplicationContext context: Context,
            @UploaderQualifier metaDataExtractor: VideoMetaDataExtractor
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
