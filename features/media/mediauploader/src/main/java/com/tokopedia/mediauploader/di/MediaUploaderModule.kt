package com.tokopedia.mediauploader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.data.FileUploadServices
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

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
    ): DataPolicyUseCase {
        return DataPolicyUseCase(repository, Dispatchers.IO)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderUseCase(
            services: FileUploadServices
    ): MediaUploaderUseCase {
        return MediaUploaderUseCase(services, Dispatchers.IO)
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