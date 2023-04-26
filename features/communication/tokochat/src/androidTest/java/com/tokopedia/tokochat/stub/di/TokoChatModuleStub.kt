package com.tokopedia.tokochat.stub.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatUploadImageApi
import com.tokopedia.tokochat.stub.common.TokoChatCacheManagerStub
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatModuleStub {

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@TokoChatQualifier context: Context): UserSessionInterface {
        return UserSession(context)
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

    @Provides
    @TokoChatQualifier
    fun provideTokoChatRepositoryStub(
        @TokoChatQualifier retrofit: Retrofit,
        @TokoChatQualifier context: Context,
        @TokoChatQualifier babbleCourierClient: BabbleCourierClient
    ): TokoChatRepositoryStub {
        return TokoChatRepositoryStub(retrofit, context, babbleCourierClient)
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
}
