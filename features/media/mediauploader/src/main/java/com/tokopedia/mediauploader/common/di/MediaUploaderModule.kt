package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.data.ImageUploadServices
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.video.SimpleUploaderManager
import com.tokopedia.mediauploader.video.data.VideoUploadServices
import com.tokopedia.mediauploader.video.domain.GetSimpleUploaderUseCase
import com.tokopedia.mediauploader.video.domain.GetVideoPolicyUseCase
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
        simpleUploader: SimpleUploaderManager
    ): UploaderUseCase {
        return UploaderUseCase(
            imageUploader,
            simpleUploader
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
        services: ImageUploadServices
    ): GetImageUploaderUseCase {
        return GetImageUploaderUseCase(services)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideImageUploaderManager(
        imagePolicyUseCase: GetImagePolicyUseCase,
        imageUploaderUseCase: GetImageUploaderUseCase
    ): ImageUploaderManager {
        return ImageUploaderManager(
            imagePolicyUseCase,
            imageUploaderUseCase
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
        services: VideoUploadServices
    ): GetSimpleUploaderUseCase {
        return GetSimpleUploaderUseCase(services)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideSimpleUploaderManager(
        videoPolicyUseCase: GetVideoPolicyUseCase,
        simpleUploaderUseCase: GetSimpleUploaderUseCase
    ): SimpleUploaderManager {
        return SimpleUploaderManager(
            videoPolicyUseCase,
            simpleUploaderUseCase
        )
    }

}