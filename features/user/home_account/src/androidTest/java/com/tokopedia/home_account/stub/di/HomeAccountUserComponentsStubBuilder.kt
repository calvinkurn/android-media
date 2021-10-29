package com.tokopedia.home_account.stub.di

import android.content.Context
import com.tokopedia.home_account.di.HomeAccountUserModules
import com.tokopedia.sessioncommon.di.SessionModule

object HomeAccountUserComponentsStubBuilder {

    fun getComponent(applicationContext: Context, activityContext: Context): HomeAccountUserComponentsStub =
        DaggerHomeAccountUserComponentsStub.builder()
            .fakeBaseAppComponent(FakeBaseAppComponentBuilder.getComponent(applicationContext))
            .homeAccountUserModules(HomeAccountUserModules(activityContext))
            .sessionModule(SessionModule())
            .homeAccountResponseModule(HomeAccountResponseModule())
            .homeAccountFakeUsecaseModule(HomeAccountFakeUsecaseModule())
            .build()
}