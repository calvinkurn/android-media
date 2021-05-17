package com.tokopedia.tokomart.search.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.search.presentation.view.SearchFragment
import dagger.Component

@SearchScope
@Component(
        modules = [SearchViewModelModule::class, SearchContextModule::class, SearchQueryParamModule::class],
        dependencies = [BaseAppComponent::class])
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)
}