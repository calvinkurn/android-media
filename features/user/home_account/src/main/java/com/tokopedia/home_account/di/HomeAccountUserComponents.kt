package com.tokopedia.home_account.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.ui.accountsettings.AccountSettingActivity
import com.tokopedia.home_account.ui.fundsAndInvestment.FundsAndInvestmentComposeActivity
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@ActivityScope
@Component(
    modules = [
        HomeAccountUserModules::class,
        HomeAccountUserUsecaseModules::class,
        HomeAccountUserViewModelModules::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface HomeAccountUserComponents {
    fun inject(view: HomeAccountUserFragment?)
    fun inject(activity: AccountSettingActivity)
    fun inject(activity: FundsAndInvestmentComposeActivity)
}
