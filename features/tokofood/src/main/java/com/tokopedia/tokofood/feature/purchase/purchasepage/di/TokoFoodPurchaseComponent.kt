package com.tokopedia.tokofood.feature.purchase.purchasepage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseFragmentOld
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.subview.TokoFoodPurchaseConsentBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodPurchaseViewModelModule::class, TokoFoodPurchaseModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodPurchaseComponent {
    fun inject(fragment: TokoFoodPurchaseFragment)
    fun inject(fragment: TokoFoodPurchaseFragmentOld)

    fun inject(bottomSheet: TokoFoodPurchaseConsentBottomSheet)
}
