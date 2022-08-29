package com.tokopedia.logout.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logout.di.module.LogoutViewModelModule
import com.tokopedia.logout.view.LogoutActivity
import dagger.Component

@ActivityScope
@Component(modules = [
    LogoutViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface LogoutComponent {
    fun inject(logoutActivity: LogoutActivity)
}