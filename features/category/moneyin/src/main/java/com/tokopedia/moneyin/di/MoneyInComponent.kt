package com.tokopedia.moneyin.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.moneyin.viewcontrollers.activity.FinalPriceActivity
import com.tokopedia.moneyin.viewcontrollers.activity.MoneyInCheckoutActivity
import com.tokopedia.moneyin.viewcontrollers.activity.MoneyInHomeActivity
import dagger.Component

@MoneyInScope
@Component(modules = [MoneyInUseCaseModule::class,
    MoneyInViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface MoneyInComponent {
    fun inject(moneyInHomeActivity: MoneyInHomeActivity)
    fun inject(finalPriceActivity: FinalPriceActivity)
    fun inject(moneyInCheckoutActivity: MoneyInCheckoutActivity)
}