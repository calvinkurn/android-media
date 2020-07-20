package com.tokopedia.deals.category.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.category.di.DealsCategoryComponent
import com.tokopedia.deals.category.di.DaggerDealsCategoryComponent
import com.tokopedia.deals.common.ui.activity.DealsBaseBrandCategoryActivity
import com.tokopedia.deals.search.model.response.Category

/**
 * @author by firman on 22/06/20
 */

class DealsCategoryActivity : DealsBaseBrandCategoryActivity(), HasComponent<DealsCategoryComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            if (!uri.getQueryParameter(PARAM_CATEGORY_ID).isNullOrEmpty()) {
                intent.putExtra(EXTRA_CATEGORY_ID, uri.getQueryParameter(PARAM_CATEGORY_ID))
            }
        }

        super.onCreate(savedInstanceState)
    }
    override fun isSearchAble(): Boolean = false

    override fun getComponent(): DealsCategoryComponent {
        return DaggerDealsCategoryComponent.builder()
                .dealsComponent(getDealsComponent())
                .build()
    }

    override fun getPageTAG(): String = TAG

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

        const val TAG = "DealsCategoryActivity"
        fun getCallingIntent(context: Context, categoryId: String? = null): Intent = Intent(context, DealsCategoryActivity::class.java).apply {
            putExtra(EXTRA_CATEGORY_ID, categoryId)
        }
    }

}
