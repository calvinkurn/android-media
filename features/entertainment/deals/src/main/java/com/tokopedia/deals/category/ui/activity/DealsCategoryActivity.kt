package com.tokopedia.deals.category.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.category.di.DealsCategoryComponent
import com.tokopedia.deals.category.di.DaggerDealsCategoryComponent
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.ui.activity.DealsBaseBrandCategoryActivity
import com.tokopedia.deals.search.model.response.Category
import javax.inject.Inject

/**
 * @author by firman on 22/06/20
 */

class DealsCategoryActivity : DealsBaseBrandCategoryActivity(), HasComponent<DealsCategoryComponent> {

    @Inject
    lateinit var analytics: DealsAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            if (!uri.getQueryParameter(PARAM_CATEGORY_ID).isNullOrEmpty()) {
                intent.putExtra(EXTRA_CATEGORY_ID, uri.getQueryParameter(PARAM_CATEGORY_ID))
            }
        }

        isLandmarkPage = intent.getBooleanExtra(IS_LANDMARK_PAGE, false)
        super.onCreate(savedInstanceState)
        initInjector()
    }
    override fun isSearchAble(): Boolean = false

    override fun getComponent(): DealsCategoryComponent {
        return DaggerDealsCategoryComponent.builder()
                .dealsComponent(getDealsComponent())
                .build()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun tabAnalytics(categoryName: String, position: Int) {
        if(this::analytics.isInitialized) {
            analytics.eventClickCategoryTabCategoryPage(categoryName)
        }
    }

    override fun getPageTAG(): String = TAG ?: "DealsCategoryActivity"

    override fun findCategoryPosition(categoryId: String): Int? {
        if (categoryId.isEmpty()) return 0
        childCategoryList.forEachIndexed { index, s ->
            if (s == categoryId) {
                return index
            }
        }
        return null
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
        const val PARAM_CATEGORY_ID = "category_id"
        const val IS_LANDMARK_PAGE = "is_landmark_page"

        val TAG = DealsCategoryActivity::class.simpleName
        fun getCallingIntent(context: Context, categoryId: String? = null,
                             isLandmarkPage: Boolean = false): Intent = Intent(context, DealsCategoryActivity::class.java).apply {
            putExtra(EXTRA_CATEGORY_ID, categoryId)
            putExtra(IS_LANDMARK_PAGE, isLandmarkPage)
        }
    }

}
