package com.tokopedia.tokochat.stub.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.common.util.TokoChatCacheManager
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatUploadImageApi
import com.tokopedia.tokochat.stub.common.TokoChatCacheManagerStub
import com.tokopedia.tokochat.stub.common.TokoChatNetworkUtilStub
import com.tokopedia.tokochat.stub.common.UserSessionStub
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatModuleStub {

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@TokoChatQualifier context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatImageRepository(
        tokoChatImageApi: TokoChatImageApi,
        tokoChatDownloadImageApi: TokoChatDownloadImageApi,
        tokoChatUploadImageApi: TokoChatUploadImageApi
    ): TokoChatImageRepository {
        return TokoChatImageRepository(
            tokoChatImageApi,
            tokoChatDownloadImageApi,
            tokoChatUploadImageApi
        )
    }

    @Provides
    @TokoChatQualifier
    fun providesRemoteConfig(
        @TokoChatQualifier context: Context
    ): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ActivityScope
    @Provides
    fun provideRemoteConfig(@TokoChatQualifier remoteConfig: RemoteConfig): RemoteConfig {
        return remoteConfig
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatRepositoryStub(
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier context: Context,
        @TokoChatQualifier babbleCourierClient: BabbleCourierClient,
        @TokoChatQualifier remoteConfig: RemoteConfig
    ): TokoChatRepositoryStub {
        return TokoChatRepositoryStub(retrofit, context, babbleCourierClient, remoteConfig)
    }

    @Provides
    @TokoChatQualifier
    fun provideTokoChatRepository(
        @TokoChatQualifier tokoChatRepositoryStub: TokoChatRepositoryStub
    ): TokoChatRepository {
        return tokoChatRepositoryStub
    }

    @Provides
    @ActivityScope
    fun provideTokoChatCacheManger(): TokoChatCacheManager {
        return TokoChatCacheManagerStub()
    }

    @ActivityScope
    @Provides
    fun provideTokoChatNetworkUtil(): TokoChatNetworkUtil {
        return TokoChatNetworkUtilStub()
    }
}
