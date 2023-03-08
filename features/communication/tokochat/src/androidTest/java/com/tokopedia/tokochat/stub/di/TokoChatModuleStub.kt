package com.tokopedia.tokochat.stub.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TokoChatModuleStub {

    @Provides
    @TokoChatQualifier
    fun provideUserSessionInterface(@TokoChatQualifier context: Context): UserSessionInterface {
        return UserSession(context)
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
}
