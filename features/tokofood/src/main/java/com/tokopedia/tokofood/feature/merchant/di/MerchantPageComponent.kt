package com.tokopedia.tokofood.feature.merchant.di

import com.tokopedia.tokofood.common.di.TokoFoodComponent
import com.tokopedia.tokofood.common.di.TokoFoodModule
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.MerchantPageFragment
import dagger.Component

@MerchantPageScope
@Component(modules = [TokoFoodModule::class, MerchantPageViewModelModule::class], dependencies = [TokoFoodComponent::class])
interface MerchantPageComponent {
    fun inject(fragment: MerchantPageFragment)
}