package com.tokopedia.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.stub.UserSessionStub
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Provides

class UniversalShareUseCaseModuleStub : UniversalShareUseCaseModule() {

    @Provides
    override fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }
}
