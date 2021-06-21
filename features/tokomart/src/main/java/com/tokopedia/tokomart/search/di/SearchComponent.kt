package com.tokopedia.tokomart.search.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.search.presentation.view.SearchFragment
import com.tokopedia.tokomart.searchcategory.di.UserSessionModule
import dagger.Component

@SearchScope
@Component(
        modules = [
            SearchViewModelModule::class,
            SearchContextModule::class,
            SearchQueryParamModule::class,
            UserSessionModule::class,
                  ],
        dependencies = [BaseAppComponent::class])
interface SearchComponent {

    fun inject(searchFragment: SearchFragment)
}