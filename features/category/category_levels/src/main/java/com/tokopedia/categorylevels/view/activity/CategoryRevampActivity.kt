package com.tokopedia.categorylevels.view.activity

import android.os.Bundle
import com.tokopedia.categorylevels.di.CategoryRepoProvider
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity

class CategoryRevampActivity : DiscoveryActivity() {

    private var departmentId: String = ""
    private var departmentName: String = ""
    private var categoryUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewRepoProvider() : RepositoryProvider {
        intent.data?.let {
            categoryUrl = it.toString()
            departmentId = it.pathSegments[0]
            departmentName = it.getQueryParameter(CategoryNavActivity.EXTRA_CATEGORY_NAME) ?: ""
        }
        return CategoryRepoProvider(departmentName, departmentId)
    }
}