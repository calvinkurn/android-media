package com.tokopedia.tokofood.feature.purchase.purchasepage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentBottomSheet
import dagger.Component

@TokoFoodPurchaseScope
@Component(modules = [TokoFoodPurchaseViewModelModule::class, TokoFoodPurchaseModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodPurchaseComponent {
    fun inject(fragment: TokoFoodPurchaseFragment)
    fun inject(bottomSheet: TokoFoodPurchaseConsentBottomSheet)
}
