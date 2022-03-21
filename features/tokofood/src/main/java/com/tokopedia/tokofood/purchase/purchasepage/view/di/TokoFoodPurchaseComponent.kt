package com.tokopedia.tokofood.purchase.purchasepage.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseFragment
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodPurchaseViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodPurchaseComponent {
    fun inject(fragment: TokoFoodPurchaseFragment)
}