package com.tokopedia.home_account.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.view.fragment.FundsAndInvestmentFragment
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component

/**
 * @author by nisie on 10/15/18.
 */
@ActivityScope
@SessionCommonScope
@Component(modules = [HomeAccountUserModules::class,
    HomeAccountUserUsecaseModules::class,
    HomeAccountUserViewModelModules::class,
    HomeAccountUserQueryModules::class,
    SessionModule::class], dependencies = [BaseAppComponent::class])
interface HomeAccountUserComponents {
    fun inject(view: HomeAccountUserFragment?)
    fun inject(view: FundsAndInvestmentFragment?)
}