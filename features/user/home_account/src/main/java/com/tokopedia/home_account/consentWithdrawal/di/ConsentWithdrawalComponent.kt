package com.tokopedia.home_account.consentWithdrawal.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.consentWithdrawal.di.modules.ConsentWithdrawalModule
import com.tokopedia.home_account.consentWithdrawal.di.modules.ConsentWithdrawalViewModelModule
import com.tokopedia.home_account.consentWithdrawal.ui.ConsentWithdrawalFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
    ConsentWithdrawalModule::class,
    ConsentWithdrawalViewModelModule::class
],
    dependencies = [
    BaseAppComponent::class
]
)
interface ConsentWithdrawalComponent {
    fun inject(fragment: ConsentWithdrawalFragment)
}
