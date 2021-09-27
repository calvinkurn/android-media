package com.tokopedia.tokopedianow.recentpurchase.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.recentpurchase.di.module.RecentPurchaseModule
import com.tokopedia.tokopedianow.recentpurchase.di.module.RecentPurchaseViewModelModule
import com.tokopedia.tokopedianow.recentpurchase.di.scope.RecentPurchaseScope
import com.tokopedia.tokopedianow.recentpurchase.presentation.fragment.TokoNowRecentPurchaseFragment
import dagger.Component

@RecentPurchaseScope
@Component(
    modules = [
        RecentPurchaseModule::class,
        RecentPurchaseViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface RecentPurchaseComponent {

    fun inject(fragment: TokoNowRecentPurchaseFragment)
}