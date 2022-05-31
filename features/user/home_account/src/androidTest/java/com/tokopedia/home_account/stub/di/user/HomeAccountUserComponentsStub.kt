package com.tokopedia.home_account.stub.di.user

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.di.HomeAccountUserQueryModules
import com.tokopedia.home_account.di.HomeAccountUserUsecaseModules
import com.tokopedia.home_account.di.HomeAccountUserViewModelModules
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@ActivityScope
@SessionCommonScope
@Component(
    modules = [FakeHomeAccountUserModules::class,
        HomeAccountUserUsecaseModules::class,
        HomeAccountUserViewModelModules::class,
        HomeAccountUserQueryModules::class,
              SessionModule::class],
    dependencies = [FakeBaseAppComponent::class])
interface HomeAccountUserComponentsStub : HomeAccountUserComponents {

        fun userSession(): UserSessionInterface
}