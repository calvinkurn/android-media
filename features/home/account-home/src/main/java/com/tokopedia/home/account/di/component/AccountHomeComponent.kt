package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.AccountHomeModule
import com.tokopedia.home.account.di.scope.AccountHomeScope
import com.tokopedia.home.account.presentation.fragment.AccountHomeFragment

import dagger.Component

/**
 * @author okasurya on 7/20/18.
 */
@Component(modules = [AccountHomeModule::class], dependencies = [BaseAppComponent::class])
@AccountHomeScope
interface AccountHomeComponent {
    fun inject(fragment: AccountHomeFragment)
}
