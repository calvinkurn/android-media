package com.tokopedia.home_account.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.home_account.stub.di.FakeBaseAppComponent
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import dagger.Provides
import javax.inject.Named

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