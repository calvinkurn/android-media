package com.tokopedia.tokopedianow.category.presentation.activity

import android.net.Uri
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.category.di.component.CategoryComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.category.di.component.DaggerCategoryComponent
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryFragment

class TokoNowCategoryActivity: BaseTokoNowActivity(), HasComponent<CategoryComponent> {
    companion object {
        private const val PARAM_CATEGORY_L1 = "category_l1"
    }

    override fun getComponent(): CategoryComponent? {
        val uri = intent.data
        val categoryL1 = uri.getCategoryL1()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return DaggerCategoryComponent.builder()
                .baseAppComponent((applicationContext as? BaseMainApplication)?.baseAppComponent)
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryL1, queryParamMap))
                .build()
    }

    override fun getFragment() = TokoNowCategoryFragment.newInstance()

    private fun Uri?.getCategoryL1(): String = this?.getQueryParameter(PARAM_CATEGORY_L1).orEmpty()

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        return this
    }
}
