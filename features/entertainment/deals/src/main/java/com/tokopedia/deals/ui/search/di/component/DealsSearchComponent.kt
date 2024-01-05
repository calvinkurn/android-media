package com.tokopedia.deals.ui.search.di.component

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.search.di.module.DealsSearchModule
import com.tokopedia.deals.ui.search.di.module.DealsSearchViewModelModule
import com.tokopedia.deals.ui.search.ui.activity.DealsSearchActivity
import com.tokopedia.deals.ui.search.ui.fragment.DealsSearchFragment
import dagger.Component

@com.tokopedia.deals.ui.search.di.DealsSearchScope
@Component(
    modules = [
        DealsSearchModule::class,
        DealsSearchViewModelModule::class
    ],
    dependencies = [
        DealsComponent::class
    ]
)
interface DealsSearchComponent {
    fun inject(dealsSearchActivity: DealsSearchActivity)
    fun inject(dealsSearchFragment: DealsSearchFragment)
}
