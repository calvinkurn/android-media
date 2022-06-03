package com.tokopedia.home_account.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.di.HomeAccountUserQueryModules
import com.tokopedia.home_account.di.HomeAccountUserUsecaseModules
import com.tokopedia.home_account.di.HomeAccountUserViewModelModules
import com.tokopedia.home_account.stub.di.topads.FakeHomeAccountTopAdsModules
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

@ActivityScope
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