package com.tokopedia.mediauploader.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.image.di.ImageUploaderModule
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.video.di.VideoUploaderModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        MediaUploaderNetworkModule::class,
        ImageUploaderModule::class,
        VideoUploaderModule::class
    ]
)
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
        imagePolicyUseCase: GetImagePolicyUseCase,
        imageUploaderUseCase: GetImageUploaderUseCase
    ): UploaderUseCase {
        return UploaderUseCase(imagePolicyUseCase, imageUploaderUseCase)
    }

}