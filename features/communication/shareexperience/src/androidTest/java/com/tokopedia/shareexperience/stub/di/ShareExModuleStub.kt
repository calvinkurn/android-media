package com.tokopedia.shareexperience.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shareexperience.data.mapper.ShareExChannelMapper
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExResourceProviderImpl
import com.tokopedia.shareexperience.data.util.ShareExTelephonyUtil
import com.tokopedia.shareexperience.stub.ShareExChannelMapperStub
import com.tokopedia.shareexperience.stub.ShareExTelephonyUtilStub
import com.tokopedia.shareexperience.stub.common.RemoteConfigStub
import com.tokopedia.shareexperience.stub.common.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object ShareExModuleStub {

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @Provides
    @ActivityScope
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ShareExResourceProvider {
        return ShareExResourceProviderImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideRemoteConfig(): RemoteConfig {
        return RemoteConfigStub()
    }

    @Provides
    @ActivityScope
    fun provideTelephonyUtil(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface
    ): ShareExTelephonyUtil {
        return ShareExTelephonyUtilStub(context, userSession)
    }

    @Provides
    @ActivityScope
    fun provideChannelMapper(
        @ApplicationContext context: Context,
        resourceProvider: ShareExResourceProvider,
        telephony: ShareExTelephonyUtil,
        remoteConfig: RemoteConfig,
        userSession: UserSessionInterface
    ): ShareExChannelMapper {
        return ShareExChannelMapperStub(
            context,
            resourceProvider,
            telephony,
            remoteConfig,
            userSession
        )
    }
}
