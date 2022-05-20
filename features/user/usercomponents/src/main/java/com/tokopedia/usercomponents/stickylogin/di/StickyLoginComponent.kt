package com.tokopedia.usercomponents.stickylogin.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.usercomponents.stickylogin.di.module.StickyLoginModule
import com.tokopedia.usercomponents.stickylogin.di.module.StickyLoginViewModelModule
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import dagger.Component

@ActivityScope
@Component(modules = [
    StickyLoginModule::class,
    StickyLoginViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface StickyLoginComponent {
    fun inject(view: StickyLoginView)
}