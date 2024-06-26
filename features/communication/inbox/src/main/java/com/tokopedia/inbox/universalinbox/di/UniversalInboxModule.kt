package com.tokopedia.inbox.universalinbox.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.data.datastore.UniversalInboxDataStore
import com.tokopedia.inbox.universalinbox.data.datastore.UniversalInboxDataStoreImpl
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMiscMapper
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProviderImpl
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatformImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxModule {
    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): UniversalInboxResourceProvider {
        return UniversalInboxResourceProviderImpl(context)
    }

    @ActivityScope
    @Provides
    fun provideAbTestPlatform(): UniversalInboxAbPlatform {
        return UniversalInboxAbPlatformImpl(
            RemoteConfigInstance.getInstance().abTestPlatform
        )
    }

    @ActivityScope
    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): UniversalInboxDataStore {
        return UniversalInboxDataStoreImpl(context)
    }

    @ActivityScope
    @Provides
    fun provideMiscMenuMapper(): UniversalInboxMiscMapper {
        return UniversalInboxMiscMapper()
    }
}
