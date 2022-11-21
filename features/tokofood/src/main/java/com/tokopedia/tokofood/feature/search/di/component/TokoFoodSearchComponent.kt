package com.tokopedia.tokofood.feature.search.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.feature.search.di.module.TokoFoodSearchModule
import com.tokopedia.tokofood.feature.search.container.presentation.fragment.SearchContainerFragment
import com.tokopedia.tokofood.feature.search.di.scope.TokoFoodSearchScope
import com.tokopedia.tokofood.feature.search.initialstate.presentation.fragment.InitialSearchStateFragment
import com.tokopedia.tokofood.feature.search.searchresult.presentation.bottomsheet.TokofoodQuickPriceRangeBottomsheet
import com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment.SearchResultFragment
import dagger.Component

@TokoFoodSearchScope
@Component(modules = [TokoFoodSearchModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodSearchComponent {
    fun inject(searchContainerFragment: SearchContainerFragment)
    fun inject(initialSearchStateFragment: InitialSearchStateFragment)
    fun inject(searchResultFragment: SearchResultFragment)
    fun inject(bottomSheet: TokofoodQuickPriceRangeBottomsheet)
}