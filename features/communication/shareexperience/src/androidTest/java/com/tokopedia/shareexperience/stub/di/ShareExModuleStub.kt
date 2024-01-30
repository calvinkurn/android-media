package com.tokopedia.shareexperience.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExResourceProviderImpl
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

    @ActivityScope
    @Provides
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ShareExResourceProvider {
        return ShareExResourceProviderImpl(context)
    }
}
