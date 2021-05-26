package com.tokopedia.digital.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageSearchFragment
import com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@RechargeHomepageScope
@Component(modules = [RechargeHomepageModule::class, RechargeHomepageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface RechargeHomepageComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun digitalHomePageDispatchersProvider(): CoroutineDispatchers

    fun inject(digitalHomePageSearchFragment: DigitalHomePageSearchFragment)

    fun inject(rechargeHomepageFragment: RechargeHomepageFragment)
}