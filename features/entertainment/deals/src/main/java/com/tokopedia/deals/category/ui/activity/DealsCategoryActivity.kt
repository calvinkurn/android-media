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

    override fun getPageTAG(): String = TAG

    companion object {
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"
        const val PARAM_CATEGORY_ID = "category_id"

        const val TAG = "DealsCategoryActivity"
        fun getCallingIntent(context: Context, categoryId: String? = null): Intent = Intent(context, DealsCategoryActivity::class.java).apply {
            putExtra(EXTRA_CATEGORY_ID, categoryId)
        }
    }

}
