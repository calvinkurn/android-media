package com.tokopedia.home_account.stub.di.user

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.di.HomeAccountUserUsecaseModules
import com.tokopedia.home_account.di.HomeAccountUserViewModelModules
import com.tokopedia.home_account.main.HomeAccountUiTest
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@ActivityScope
@Component(
    modules = [
        FakeHomeAccountUserModules::class,
        HomeAccountUserUsecaseModules::class,
        HomeAccountUserViewModelModules::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface HomeAccountUserComponentsStub : HomeAccountUserComponents {

    fun userSession(): UserSessionInterface
    fun inject(test: HomeAccountUiTest)
}
