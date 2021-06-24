package com.tokopedia.tokomart.search.presentation.view

import android.os.Bundle
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.search.di.DaggerSearchComponent
import com.tokopedia.tokomart.search.di.SearchComponent
import com.tokopedia.tokomart.search.di.SearchContextModule
import com.tokopedia.tokomart.search.di.SearchQueryParamModule
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryActivity

class SearchActivity: BaseSearchCategoryActivity(), HasComponent<SearchComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomart_search_category)

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