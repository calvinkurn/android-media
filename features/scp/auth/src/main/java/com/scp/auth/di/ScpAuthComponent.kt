package com.scp.auth.di

import com.scp.auth.ScpAuthActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Component

@ActivityScope
@Component(modules = [ScpModules::class, ScpAuthViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ScpAuthComponent {
    fun inject(fragment: ScpAuthActivity)
}
