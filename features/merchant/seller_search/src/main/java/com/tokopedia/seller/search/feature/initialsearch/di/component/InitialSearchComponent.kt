package com.tokopedia.seller.search.feature.initialsearch.di.component

import com.tokopedia.seller.search.common.di.component.GlobalSearchSellerComponent
import com.tokopedia.seller.search.feature.initialsearch.di.module.InitialSearchModule
import com.tokopedia.seller.search.feature.initialsearch.di.scope.InitialSearchScope
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchActivity
import com.tokopedia.seller.search.feature.initialsearch.view.activity.InitialSellerSearchComposeActivity
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchComposeFragment
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchComposeFragment
import com.tokopedia.seller.search.feature.suggestion.view.fragment.SuggestionSearchFragment
import dagger.Component

@InitialSearchScope
@Component(modules = [InitialSearchModule::class], dependencies = [GlobalSearchSellerComponent::class])
interface InitialSearchComponent {
    fun inject(initialSearchFragment: InitialSearchFragment)
    fun inject(suggestionSearchFragment: SuggestionSearchFragment)
    fun inject(sellerSearchActivity: InitialSellerSearchActivity)
    fun inject(initialSellerSearchComposeActivity: InitialSellerSearchComposeActivity)
    fun inject(initialSearchComposeFragment: InitialSearchComposeFragment)
    fun inject(suggestionSearchComposeFragment: SuggestionSearchComposeFragment)
}
