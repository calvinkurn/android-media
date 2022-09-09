package com.tokopedia.tokofood.feature.search.searchresult.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.common.di.TokoFoodModule
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.searchresult.di.module.SearchResultViewModelModule
import com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment.SearchResultFragment
import dagger.Component

@TokoFoodScope
@Component(modules = [SearchResultViewModelModule::class, TokoFoodModule::class], dependencies = [BaseAppComponent::class])
internal interface SearchResultComponent {
    fun inject(fragment: SearchResultFragment)
}