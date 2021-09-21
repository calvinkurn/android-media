package com.tokopedia.home_account.linkaccount.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_account.linkaccount.di.module.LinkAccountModule
import com.tokopedia.home_account.linkaccount.di.module.LinkAccountViewModelModule
import com.tokopedia.home_account.linkaccount.view.LinkAccountFragment
import dagger.Component

@LinkAccountScope
@Component(modules = [
    LinkAccountModule::class,
    LinkAccountViewModelModule::class], dependencies = [BaseAppComponent::class])
interface LinkAccountComponent {
    fun inject(fragment: LinkAccountFragment)
}