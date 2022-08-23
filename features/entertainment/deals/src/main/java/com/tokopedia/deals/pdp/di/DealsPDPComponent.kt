package com.tokopedia.deals.pdp.di

import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPFragment
import dagger.Component

@DealsPDPScope
@Component(
    modules = [
        DealsPDPViewModelModule::class
    ], dependencies = [
        DealsComponent::class
    ]
)
interface DealsPDPComponent {
    fun inject(dealsPDPFragment: DealsPDPFragment)
    fun inject(dealsPDPActivity: DealsPDPActivity)
}