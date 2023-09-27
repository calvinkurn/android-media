package com.tokopedia.universal_sharing.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.stub.common.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UniversalSharingModuleStub {

    @ActivityScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionStub {
        return UserSessionStub(context)
    }

    @ActivityScope
    @Provides
    fun provideUserSession(
        userSessionStub: UserSessionStub
    ): UserSessionInterface {
        return userSessionStub
    }
}
