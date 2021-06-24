package com.tokopedia.tokopedianow.search.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.search.presentation.view.SearchFragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
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