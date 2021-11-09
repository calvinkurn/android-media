package com.tokopedia.home_account.stub.di

import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.di.*
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

@HomeAccountUserScope
@SessionCommonScope
@Component(modules = [
    FakeHomeAccountUserModules::class,
    HomeAccountUserViewModelModules::class,
    HomeAccountResponseModule::class,
    SessionModule::class,
    RecommendationCoroutineModule::class,
    HomeAccountFakeUsecaseModule::class],
    dependencies = [FakeBaseAppComponent::class])
interface HomeAccountUserComponentsStub: HomeAccountUserComponents {
    fun inject(view: HomeAccountTest)
}