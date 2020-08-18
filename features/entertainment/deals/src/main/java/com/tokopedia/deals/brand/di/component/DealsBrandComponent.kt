package com.tokopedia.deals.brand.di.component

import com.tokopedia.deals.brand.di.DealsBrandScope
import com.tokopedia.deals.brand.di.module.DealsBrandModule
import com.tokopedia.deals.brand.di.module.DealsBrandViewModelModule
import com.tokopedia.deals.brand.ui.activity.DealsBrandActivity
import com.tokopedia.deals.brand.ui.fragment.DealsBrandFragment
import com.tokopedia.deals.common.di.DealsComponent
import dagger.Component

@DealsBrandScope
@Component(modules = [
    DealsBrandModule::class,
    DealsBrandViewModelModule::class
], dependencies = [
    DealsComponent::class
])
interface DealsBrandComponent {
    fun inject(activity: DealsBrandActivity)
    fun inject(fragment: DealsBrandFragment)
}