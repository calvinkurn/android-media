package com.tokopedia.deals.search.di.component

import com.tokopedia.deals.common.di.DealsComponent
import com.tokopedia.deals.search.di.DealsSearchScope
import com.tokopedia.deals.search.di.module.DealsSearchModule
import com.tokopedia.deals.search.di.module.DealsSearchViewModelModule
import com.tokopedia.deals.search.ui.activity.DealsSearchActivity
import com.tokopedia.deals.search.ui.fragment.DealsSearchFragment
import dagger.Component

@DealsSearchScope
@Component(modules = [
    DealsSearchModule::class,
    DealsSearchViewModelModule::class
], dependencies = [
    DealsComponent::class
])
interface DealsSearchComponent {
    fun inject(dealsSearchActivity: DealsSearchActivity)
    fun inject(dealsSearchFragment: DealsSearchFragment)
}