package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.internal.MediaUploaderUrl
import com.tokopedia.mediauploader.common.internal.SourcePolicyManager
import com.tokopedia.mediauploader.common.internal.SourcePolicyManagerImpl
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageSecurePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.video.LargeUploaderManager
import com.tokopedia.mediauploader.video.SimpleUploaderManager
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.domain.GetChunkCheckerUseCase
import com.tokopedia.mediauploader.video.domain.GetChunkUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
import com.tokopedia.mediauploader.video.domain.InitVideoUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetAbortUploaderUseCase
import com.tokopedia.mediauploader.video.domain.SetCompleteUploaderUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [MediaUploaderNetworkModule::class])
class MediaUploaderModule {

    @Provides
    @MediaUploaderQualifier
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideUploaderUseCase(
        imageUploader: ImageUploaderManager,
        videoUploader: VideoUploaderManager
    ): UploaderUseCase {
        return UploaderUseCase(
            imageUploader,
            videoUploader
        )
    }

    @Provides
    fun provideSourcePolicyManager(
        @ApplicationContext context: Context,
    ): SourcePolicyManager {
        return SourcePolicyManagerImpl(context)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideVideoUploaderManager(
        policyManager: SourcePolicyManager,
        policyUseCase: GetVideoPolicyUseCase,
        simpleUploader: SimpleUploaderManager,
        largeUploader: LargeUploaderManager
    ): VideoUploaderManager {
        return VideoUploaderManager(
            policyManager,
            policyUseCase,
            simpleUploader,
            largeUploader
        )
    }

    // --- image ---

    @Provides
    @MediaUploaderQualifier
    fun provideGetImagePolicyUseCase(
        repository: GraphqlRepository
    ): GetImagePolicyUseCase {
        return GetImagePolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetImageUploaderUseCase(
        services: ImageUploadServices,
        url: MediaUploaderUrl
    ): GetImageUploaderUseCase {
        return GetImageUploaderUseCase(services, url)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetImageSecurePolicyUseCase(
        repository: GraphqlRepository
    ): GetImageSecurePolicyUseCase {
        return GetImageSecurePolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideImageUploaderManager(
        policyManager: SourcePolicyManager,
        imagePolicyUseCase: GetImagePolicyUseCase,
        imageUploaderUseCase: GetImageUploaderUseCase,
        imageSecurePolicyUseCase: GetImageSecurePolicyUseCase
    ): ImageUploaderManager {
        return ImageUploaderManager(
            policyManager,
            imagePolicyUseCase,
            imageUploaderUseCase,
            imageSecurePolicyUseCase
        )
    }

    // --- simple video ---

    @Provides
    @MediaUploaderQualifier
    fun provideGetVideoPolicyUseCase(
        repository: GraphqlRepository
    ): GetVideoPolicyUseCase {
        return GetVideoPolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetSimpleVideoUploaderUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): GetSimpleUploaderUseCase {
        return GetSimpleUploaderUseCase(services, url)
    }

    // --- large video ---

    @Provides
    @MediaUploaderQualifier
    fun provideInitVideoUploaderUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): InitVideoUploaderUseCase {
        return InitVideoUploaderUseCase(services, url)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetChunkCheckerUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): GetChunkCheckerUseCase {
        return GetChunkCheckerUseCase(services, url)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideGetChunkUploaderUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): GetChunkUploaderUseCase {
        return GetChunkUploaderUseCase(services, url)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideSetCompleteUploaderUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): SetCompleteUploaderUseCase {
        return SetCompleteUploaderUseCase(services, url)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideSetAbortUploaderUseCase(
        services: VideoUploadServices,
        url: MediaUploaderUrl
    ): SetAbortUploaderUseCase {
        return SetAbortUploaderUseCase(services, url)
    }

}
