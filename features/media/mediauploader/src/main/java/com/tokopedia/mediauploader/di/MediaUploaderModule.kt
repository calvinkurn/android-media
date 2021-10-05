package com.tokopedia.mediauploader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.data.ImageUploadServices
import com.tokopedia.mediauploader.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module (includes = [MediaUploaderNetworkModule::class])
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
    fun provideDataPolicyUseCase(
            repository: GraphqlRepository
    ): GetImagePolicyUseCase {
        return GetImagePolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderUseCase(
            services: ImageUploadServices
    ): GetImageUploaderUseCase {
        return GetImageUploaderUseCase(services)
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