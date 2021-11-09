package com.tokopedia.deals.brand_detail.di

import com.tokopedia.deals.brand_detail.ui.fragment.DealsBrandDetailFragment
import com.tokopedia.deals.common.di.DealsComponent
import dagger.Component


@DealsBrandDetailScope
@Component(modules = [DealsBrandDetailViewModelModule::class],
        dependencies = [DealsComponent::class])
interface DealsBrandDetailComponent {

    fun inject(dealsBrandDetailFragment: DealsBrandDetailFragment)
}