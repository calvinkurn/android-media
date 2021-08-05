package com.tokopedia.linkaccount.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.linkaccount.di.module.LinkAccountModule
import com.tokopedia.linkaccount.di.module.LinkAccountViewModelModule
import com.tokopedia.linkaccount.view.LinkAccountFragment
import dagger.Component

@LinkAccountScope
@Component(modules = [
    LinkAccountModule::class,
    LinkAccountViewModelModule::class], dependencies = [BaseAppComponent::class])
interface LinkAccountComponent {
    fun inject(fragment: LinkAccountFragment)
}