package com.tokopedia.tokomart.category.presentation.view

import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.di.CategoryContextModule
import com.tokopedia.tokomart.category.di.CategoryParamModule
import com.tokopedia.tokomart.category.di.DaggerCategoryComponent
import com.tokopedia.tokomart.search.di.SearchQueryParamModule
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryActivity

class CategoryActivity: BaseSearchCategoryActivity(), HasComponent<CategoryComponent> {

    override fun getNewFragment() = CategoryFragment.create()

    override fun getComponent(): CategoryComponent {
        val uri = intent.data
        val categoryId = 0 // Get category id from applink intent
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        return DaggerCategoryComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryId, queryParamMap))
                .build()
    }
}