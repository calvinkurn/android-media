package com.tokopedia.tokopedianow.oldcategory.presentation.view

import android.net.Uri
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.tokopedianow.oldcategory.di.CategoryComponent
import com.tokopedia.tokopedianow.oldcategory.di.CategoryContextModule
import com.tokopedia.tokopedianow.oldcategory.di.CategoryParamModule
import com.tokopedia.tokopedianow.oldcategory.di.DaggerCategoryComponent
import com.tokopedia.tokopedianow.searchcategory.presentation.view.BaseSearchCategoryActivity

class TokoNowCategoryActivity: BaseSearchCategoryActivity(), HasComponent<CategoryComponent> {

    override fun getComponent(): CategoryComponent {
        val uri = intent.data
        val categoryL1 = uri.getCategoryL1()
        val categoryL2 = uri.getCategoryL2()
        val serviceType = uri.getServiceType()
        val queryParamMap = URLParser(uri.toString()).paramKeyValueMapDecoded
        queryParamMap.removeCategoryEntries()

        return DaggerCategoryComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .categoryContextModule(CategoryContextModule(this))
                .categoryParamModule(CategoryParamModule(categoryL1, categoryL2, serviceType, queryParamMap))
                .build()
    }

    override fun getFragment() = TokoNowCategoryFragment.create()

    private fun Uri?.getCategoryL1(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L1) ?: ""

    private fun Uri?.getCategoryL2(): String =
            this?.getQueryParameter(PARAM_CATEGORY_L2) ?: ""

    private fun Uri?.getServiceType(): String =
            this?.getQueryParameter(PARAM_SERVICE_TYPE) ?: ""

    private fun MutableMap<String, String>.removeCategoryEntries(): Map<String, String> {
        remove(PARAM_CATEGORY_L1)
        remove(PARAM_CATEGORY_L2)

        return this
    }

    companion object {
        private const val PARAM_CATEGORY_L1 = "category_l1"
        private const val PARAM_CATEGORY_L2 = "category_l2"
        private const val PARAM_SERVICE_TYPE = "service_type"
    }
}
