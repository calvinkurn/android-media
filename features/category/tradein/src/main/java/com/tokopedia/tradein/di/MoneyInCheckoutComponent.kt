package com.tokopedia.tradein.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tradein.view.viewcontrollers.MoneyInCheckoutActivity
import dagger.Component

@MoneyInModuleScope
@Component(modules = [MoneyInUseCaseModule::class],
        dependencies = [BaseAppComponent::class])
interface MoneyInCheckoutComponent {
    fun inject(activity: MoneyInCheckoutActivity)
}