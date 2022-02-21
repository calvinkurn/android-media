package com.tokopedia.home_account.stub.di.user

import com.tokopedia.home_account.di.*
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@HomeAccountUserScope
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