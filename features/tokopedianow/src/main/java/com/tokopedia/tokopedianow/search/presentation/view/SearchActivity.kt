package com.tokopedia.tokopedianow.search.presentation.view

import android.os.Bundle
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.search.di.DaggerSearchComponent
import com.tokopedia.tokopedianow.search.di.SearchComponent
import com.tokopedia.tokopedianow.search.di.SearchContextModule
import com.tokopedia.tokopedianow.search.di.SearchQueryParamModule
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryActivity

class SearchActivity: BaseSearchCategoryActivity(), HasComponent<SearchComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_search_category)

        val fragment = SearchFragment.create()

        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment)
                .commit()
    }

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