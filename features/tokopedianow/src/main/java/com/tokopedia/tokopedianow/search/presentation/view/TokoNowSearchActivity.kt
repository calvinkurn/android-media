package com.tokopedia.tokopedianow.search.presentation.view

import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.search.di.DaggerSearchComponent
import com.tokopedia.tokopedianow.search.di.SearchComponent
import com.tokopedia.tokopedianow.search.di.SearchContextModule
import com.tokopedia.tokopedianow.search.di.SearchQueryParamModule
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryActivity

class TokoNowSearchActivity: BaseSearchCategoryActivity(), HasComponent<SearchComponent> {

    override fun getComponent(): SearchComponent {
        val uri = intent.data
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        return DaggerSearchComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .searchContextModule(SearchContextModule(this))
                .searchQueryParamModule(SearchQueryParamModule(queryParamMap))
                .build()
    }

    override fun getFragment() = TokoNowSearchFragment.create()
}