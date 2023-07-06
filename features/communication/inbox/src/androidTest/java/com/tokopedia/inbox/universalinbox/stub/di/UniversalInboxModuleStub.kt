package com.tokopedia.inbox.universalinbox.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.stub.common.UniversalInboxMenuMapperStub
import com.tokopedia.inbox.universalinbox.stub.common.UserSessionStub
import com.tokopedia.inbox.universalinbox.stub.common.util.FakeAbTestPlatformImpl
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProviderImpl
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.UniversalInboxMenuMapper
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxModuleStub {

    @Provides
    @ActivityScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
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
        return FakeAbTestPlatformImpl()
    }

    @ActivityScope
    @Provides
    fun provideUniversalInboxMenuMapper(
        resourceProvider: UniversalInboxResourceProvider,
        abTestPlatform: UniversalInboxAbPlatform
    ): UniversalInboxMenuMapper {
        return UniversalInboxMenuMapperStub(
            resourceProvider,
            abTestPlatform
        )
    }
}
