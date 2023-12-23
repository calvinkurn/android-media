package com.tokopedia.deals.ui.brand_detail.di

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.brand_detail.ui.fragment.DealsBrandDetailFragment
import dagger.Component

@DealsBrandDetailScope
@Component(
    modules = [DealsBrandDetailViewModelModule::class],
    dependencies = [DealsComponent::class]
)
interface DealsBrandDetailComponent {

    fun inject(dealsBrandDetailFragment: DealsBrandDetailFragment)
}
