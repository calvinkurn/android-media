package com.tokopedia.logout.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logout.di.module.LogoutViewModelModule
import com.tokopedia.logout.view.LogoutActivity
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(modules = [
    LogoutViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface LogoutComponent {
    fun inject(logoutActivity: LogoutActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun baseComponent(component: BaseAppComponent): Builder
        fun build(): LogoutComponent
    }
}