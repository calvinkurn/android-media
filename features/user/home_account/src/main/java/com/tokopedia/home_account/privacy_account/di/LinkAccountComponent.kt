package com.tokopedia.home_account.privacy_account.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.privacy_account.di.module.LinkAccountModule
import com.tokopedia.home_account.privacy_account.di.module.LinkAccountViewModelModule
import com.tokopedia.home_account.privacy_account.view.LinkAccountFragment
import com.tokopedia.home_account.privacy_account.view.LinkAccountWebviewFragment
import com.tokopedia.home_account.privacy_account.view.PrivacyAccountFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    LinkAccountModule::class,
    LinkAccountViewModelModule::class], dependencies = [BaseAppComponent::class])
interface LinkAccountComponent {
    fun inject(fragment: LinkAccountFragment)
    fun inject(fragment: LinkAccountWebviewFragment)
    fun inject(fragment: PrivacyAccountFragment)
}