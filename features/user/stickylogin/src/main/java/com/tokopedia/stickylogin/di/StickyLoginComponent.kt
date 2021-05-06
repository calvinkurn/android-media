package com.tokopedia.stickylogin.di

import com.tokopedia.stickylogin.di.module.StickyLoginModule
import com.tokopedia.stickylogin.di.module.StickyLoginUseCaseModule
import com.tokopedia.stickylogin.di.module.StickyLoginViewModelModule
import com.tokopedia.stickylogin.view.StickyLoginView
import dagger.Component

@StickyLoginScope
@Component(modules = [
    StickyLoginModule::class,
    StickyLoginUseCaseModule::class,
    StickyLoginViewModelModule::class
])
interface StickyLoginComponent {
    fun inject(view: StickyLoginView)
}