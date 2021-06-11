package com.tokopedia.tokomart.category.presentation.view

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.category.di.CategoryComponent
import com.tokopedia.tokomart.category.di.CategoryContextModule
import com.tokopedia.tokomart.category.di.CategoryParamModule
import com.tokopedia.tokomart.category.di.DaggerCategoryComponent
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryActivity

class CategoryActivity: BaseSearchCategoryActivity(), HasComponent<CategoryComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomart_search_category)

        val fragment = CategoryFragment.create()

        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, fragment)
                .commit()
    }

    override fun getComponent(): CategoryComponent {
        val uri = intent.data
        val categoryIdL1 = uri.getCategoryIdL1()
        val categoryIdL2 = uri.getCategoryIdL2()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded

        return DaggerCategoryComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryIdL1, categoryIdL2, queryParamMap))
                .build()
    }

    private fun Uri?.getCategoryIdL1(): String =
            this?.getQueryParameter(PARAM_CATEGORY_ID_L1) ?: ""

    private fun Uri?.getCategoryIdL2(): String =
            this?.getQueryParameter(PARAM_CATEGORY_ID_L2) ?: ""

    companion object {
        private const val PARAM_CATEGORY_ID_L1 = "category_id_l1"
        private const val PARAM_CATEGORY_ID_L2 = "category_id_l2"
    }
}