package com.tokopedia.tokofood.feature.merchant.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.common.di.TokoFoodComponent
import com.tokopedia.tokofood.common.di.TokoFoodModule
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.OrderCustomizationFragment
import dagger.Component

@MerchantPageScope
@Component(modules = [TokoFoodModule::class, MerchantPageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface MerchantPageComponent {
    fun inject(fragment: MerchantPageFragment)
    fun inject(fragment: OrderCustomizationFragment)
}