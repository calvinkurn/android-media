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
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryMainFragment

class TokoNowCategoryActivity: BaseTokoNowActivity(), HasComponent<CategoryComponent> {
    companion object {
        private const val PARAM_CATEGORY_L1 = "category_l1"
        private const val PARAM_CATEGORY_L2 = "category_l2"
        private const val PARAM_SERVICE_TYPE = "service_type"
    }

    override fun getComponent(): CategoryComponent? {
        val uri = intent.data
        val categoryL1 = uri.getCategoryL1()
        val categoryL2 = uri.getCategoryL2()
        val serviceType = uri.getServiceType()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return DaggerCategoryComponent.builder()
                .baseAppComponent((applicationContext as? BaseMainApplication)?.baseAppComponent)
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryL1, categoryL2, serviceType, queryParamMap))
                .build()
    }

    override fun getFragment() = TokoNowCategoryMainFragment.newInstance()

    private fun Uri?.getCategoryL1(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L1).orEmpty()

    private fun Uri?.getCategoryL2(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L2).orEmpty()

    private fun Uri?.getServiceType(): String =
            this?.getQueryParameter(PARAM_SERVICE_TYPE).orEmpty()

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        remove(PARAM_CATEGORY_L2)
        return this
    }
}
