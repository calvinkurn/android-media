package com.tokopedia.deals.ui.pdp.di

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPAllLocationFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPDescFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPSelectDealsQuantityFragment
import dagger.Component

@DealsPDPScope
@Component(
    modules = [
        DealsPDPModule::class,
        DealsPDPViewModelModule::class
    ],
    dependencies = [
        DealsComponent::class
    ]
)
interface DealsPDPComponent {
    fun inject(dealsPDPFragment: DealsPDPFragment)
    fun inject(dealsPDPDescFragment: DealsPDPDescFragment)
    fun inject(dealsPDPAllLocationFragment: DealsPDPAllLocationFragment)
    fun inject(dealsPDPSelectDealsQuantityFragment: DealsPDPSelectDealsQuantityFragment)
    fun inject(dealsPDPActivity: DealsPDPActivity)
}
