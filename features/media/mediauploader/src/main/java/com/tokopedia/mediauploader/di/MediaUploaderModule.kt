package com.tokopedia.mediauploader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.mediauploader.MediaRepository
import com.tokopedia.mediauploader.MediaRepositoryImpl
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.domain.DataPolicyUseCase
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase
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
    fun provideMediaRepository(): MediaRepository {
        return MediaRepositoryImpl(GraphqlInteractor.getInstance().graphqlRepository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideDataPolicyUseCase(
            repository: MediaRepository
    ): DataPolicyUseCase {
        return DataPolicyUseCase(repository)
    }

    @Provides
    @MediaUploaderQualifier
    fun provideMediaUploaderUseCase(
            services: UploaderServices
    ): MediaUploaderUseCase {
        return MediaUploaderUseCase(services)
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