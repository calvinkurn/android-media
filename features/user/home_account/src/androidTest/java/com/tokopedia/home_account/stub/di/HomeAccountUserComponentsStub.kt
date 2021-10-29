package com.tokopedia.home_account.stub.di

import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.di.HomeAccountUserModules
import com.tokopedia.home_account.di.HomeAccountUserScope
import com.tokopedia.home_account.di.HomeAccountUserViewModelModules
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

@HomeAccountUserScope
@SessionCommonScope
@Component(modules = [
    HomeAccountUserModules::class,
    HomeAccountUserViewModelModules::class,
    SessionModule::class,
    HomeAccountResponseModule::class,
    HomeAccountFakeUsecaseModule::class],
    dependencies = [FakeBaseAppComponent::class])
interface HomeAccountUserComponentsStub: HomeAccountUserComponents {
    fun inject(view: HomeAccountTest?)
}