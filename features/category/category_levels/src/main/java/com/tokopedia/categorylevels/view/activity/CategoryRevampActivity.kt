package com.tokopedia.categorylevels.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.categorylevels.analytics.CategoryRevampAnalytics
import com.tokopedia.categorylevels.di.CategoryRevampRepoProvider
import com.tokopedia.common.RepositoryProvider
import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

const val CATEGORY_LEVELS_RESULT_TRACE = "category_levels_result_trace"
const val CATEGORY_LEVELS_PLT_PREPARE_METRICS = "category_levels_plt_prepare_metrics"
const val CATEGORY_LEVELS_PLT_NETWORK_METRICS = "category_levels_plt_network_metrics"
const val CATEGORY_LEVELS_PLT_RENDER_METRICS = "category_levels_plt_render_metrics"
const val EXTRA_CATEGORY_NAME = "categoryName"

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
            departmentName = it.getQueryParameter(EXTRA_CATEGORY_NAME) ?: ""
        }
        return CategoryRevampRepoProvider((application as BaseMainApplication).baseAppComponent, departmentName, departmentId, categoryUrl)
    }

    override fun getPageIdentifier(): String {
        return departmentId
    }

    override fun getTraceName(): String {
        return CATEGORY_LEVELS_RESULT_TRACE
    }

    override fun getAnalytics(): BaseDiscoveryAnalytics {
        val userSession: UserSessionInterface = UserSession(this)
        return CategoryRevampAnalytics(getPageType(),
                getPagePath(),
                getPageIdentifier(),
                getCampaignCode(),
                getSourceIdentifier(),
                trackingQueue,
                userSession)
    }
}