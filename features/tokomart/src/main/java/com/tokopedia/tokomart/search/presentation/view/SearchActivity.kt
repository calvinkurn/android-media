package com.tokopedia.tokomart.search.presentation.view

import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokomart.search.di.DaggerSearchComponent
import com.tokopedia.tokomart.search.di.SearchComponent
import com.tokopedia.tokomart.search.di.SearchContextModule
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryActivity

class SearchActivity: BaseSearchCategoryActivity(), HasComponent<SearchComponent> {

    override fun getNewFragment() = SearchFragment.create()

    override fun getComponent(): SearchComponent {
        return DaggerSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .searchContextModule(SearchContextModule(this))
                .build()
    }
}