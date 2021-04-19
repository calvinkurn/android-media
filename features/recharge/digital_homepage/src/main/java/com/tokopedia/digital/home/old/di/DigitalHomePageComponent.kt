package com.tokopedia.digital.home.old.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.old.presentation.fragment.DigitalHomePageFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@DigitalHomePageScope
@Component(modules = [DigitalHomePageModule::class, DigitalHomePageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface DigitalHomePageComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun digitalHomePageDispatchersProvider(): CoroutineDispatchers

    fun inject(digitalHomePageFragment: DigitalHomePageFragment)
}