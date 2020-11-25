package com.tokopedia.logout.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logout.di.module.LogoutModule
import com.tokopedia.logout.di.module.LogoutUseCaseModule
import com.tokopedia.logout.di.module.LogoutViewModelModule
import com.tokopedia.logout.view.LogoutActivity
import dagger.Component

@LogoutScope
@Component(modules = [
    LogoutModule::class,
    LogoutUseCaseModule::class,
    LogoutViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface LogoutComponent {
    fun inject(logoutActivity: LogoutActivity)
}