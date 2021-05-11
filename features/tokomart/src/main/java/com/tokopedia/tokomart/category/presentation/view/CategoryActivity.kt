package com.tokopedia.tokomart.category.presentation.view

import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.di.CategoryContextModule
import com.tokopedia.tokomart.category.di.DaggerCategoryComponent
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryActivity

class CategoryActivity: BaseSearchCategoryActivity(), HasComponent<CategoryComponent> {

    override fun getNewFragment() = CategoryFragment.create()

    override fun getComponent(): CategoryComponent {
        return DaggerCategoryComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .categoryContextModule(CategoryContextModule(this))
                .build()
    }
}