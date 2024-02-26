package com.tokopedia.loginregister.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.di.UserSessionStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class FakeShopCreationModule {

    @ActivityScope
    @Provides
    fun provideUserSession(userSessionStub: UserSessionStub): UserSessionInterface {
        return userSessionStub
    }
    @ActivityScope
    @Provides
    fun provideUserSessionStub(@ApplicationContext context: Context): UserSessionStub {
        return UserSessionStub(context)
    }

    @ActivityScope
    @Provides
    fun provideShopCreationAnalytics(): ShopCreationAnalytics = ShopCreationAnalytics()

    @ActivityScope
    @Provides
    fun provideRegisterAnalytics(): RegisterAnalytics = RegisterAnalytics()
}
