package com.tokopedia.inbox.universalinbox.stub.di

import android.content.Context
import com.gojek.conversations.courier.BabbleCourierClient
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.stub.common.UniversalInboxMenuMapperStub
import com.tokopedia.inbox.universalinbox.stub.common.UserSessionStub
import com.tokopedia.inbox.universalinbox.stub.common.util.FakeAbTestPlatformImpl
import com.tokopedia.inbox.universalinbox.stub.data.repository.TokoChatRepositoryStub
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProviderImpl
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.UniversalInboxMenuMapper
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

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
    @TokoChatQualifier
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
