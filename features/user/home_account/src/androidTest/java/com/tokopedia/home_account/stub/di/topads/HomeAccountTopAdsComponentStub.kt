package com.tokopedia.home_account.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_account.di.*
import com.tokopedia.home_account.stub.di.topads.FakeHomeAccountTopAdsModules
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@HomeAccountUserScope
@SessionCommonScope
@Component(
    modules = [FakeHomeAccountTopAdsModules::class,
        HomeAccountUserUsecaseModules::class,
        HomeAccountUserViewModelModules::class,
        HomeAccountUserQueryModules::class,
        SessionModule::class],
    dependencies = [BaseAppComponent::class])
interface HomeAccountTopAdsComponentsStub : HomeAccountUserComponents {
}