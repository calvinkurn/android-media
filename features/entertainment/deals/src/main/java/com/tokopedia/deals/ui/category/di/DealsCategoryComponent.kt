package com.tokopedia.deals.ui.category.di

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.category.ui.activity.DealsCategoryActivity
import com.tokopedia.deals.ui.category.ui.fragment.DealsCategoryFragment
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
