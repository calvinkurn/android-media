package com.tokopedia.tokopedianow.category.presentation.view

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.di.CategoryComponent
import com.tokopedia.tokopedianow.category.di.CategoryContextModule
import com.tokopedia.tokopedianow.category.di.CategoryParamModule
import com.tokopedia.tokopedianow.category.di.DaggerCategoryComponent
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryActivity

class CategoryActivity: BaseSearchCategoryActivity(), HasComponent<CategoryComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedianow_search_category)

        val fragment = CategoryFragment.create()

        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment)
                .commit()
    }

    override fun getComponent(): CategoryComponent {
        val uri = intent.data
        val categoryL1 = uri.getCategoryL1()
        val categoryL2 = uri.getCategoryL2()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return DaggerCategoryComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryL1, categoryL2, queryParamMap))
                .build()
    }

    private fun Uri?.getCategoryL1(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L1) ?: ""

    private fun Uri?.getCategoryL2(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L2) ?: ""

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        remove(PARAM_CATEGORY_L2)

        return this
    }

    companion object {
        private const val PARAM_CATEGORY_L1 = "category_l1"
        private const val PARAM_CATEGORY_L2 = "category_l2"
    }
}
