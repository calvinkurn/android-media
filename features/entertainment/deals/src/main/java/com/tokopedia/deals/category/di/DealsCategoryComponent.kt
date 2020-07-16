package com.tokopedia.deals.category.di

import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.category.ui.fragment.DealsCategoryFragment
import dagger.Component


@DealsCategoryScope
@Component(modules= [DealsCategoryModule::class, DealsCategoryViewModelModule::class],
        dependencies = [DealsComponent::class])
interface DealsCategoryComponent{
    fun inject(dealsCategoryFragment: DealsCategoryFragment)
}