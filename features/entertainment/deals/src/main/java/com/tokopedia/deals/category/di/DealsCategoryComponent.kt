package com.tokopedia.deals.category.di

import com.tokopedia.deals.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.category.ui.fragment.DealsCategoryFragment
import com.tokopedia.deals.di.DealsComponent
import dagger.Component

@DealsCategoryScope
@Component(
    modules = [DealsCategoryModule::class, DealsCategoryViewModelModule::class],
    dependencies = [DealsComponent::class]
)
interface DealsCategoryComponent {
    fun inject(activity: DealsCategoryActivity)
    fun inject(dealsCategoryFragment: DealsCategoryFragment)
}
