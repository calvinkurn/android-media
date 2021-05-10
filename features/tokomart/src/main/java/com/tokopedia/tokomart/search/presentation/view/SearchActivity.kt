package com.tokopedia.tokomart.search.presentation.view

import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokomart.search.di.DaggerSearchComponent
import com.tokopedia.tokomart.search.di.SearchComponent
import com.tokopedia.tokomart.search.di.SearchContextModule
import com.tokopedia.tokomart.search.di.SearchQueryParamModule
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryActivity

class SearchActivity: BaseSearchCategoryActivity(), HasComponent<SearchComponent> {

    override fun getNewFragment() = SearchFragment.create()

    override fun getComponent(): SearchComponent {
        val uri = intent.data
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        return DaggerSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .searchContextModule(SearchContextModule(this))
                .searchQueryParamModule(SearchQueryParamModule(queryParamMap))
                .build()
    }
}