package com.tokopedia.deals.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.deals.common.ui.activity.DealsBaseBrandCategoryActivity

/**
 * @author by firman on 22/06/20
 */

class DealsCategoryActivity : DealsBaseBrandCategoryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            if (!uri.getQueryParameter(PARAM_CATEGORY_ID).isNullOrEmpty()) {
                intent.putExtra(EXTRA_CATEGORY_ID, uri.getQueryParameter(PARAM_CATEGORY_ID))
            }
        }

        isLandmarkPage = intent.getBooleanExtra(IS_LANDMARK_PAGE, false)
        super.onCreate(savedInstanceState)
    }
    override fun isSearchAble(): Boolean = false

    override fun tabAnalytics(categoryName: String, position: Int) {
        dealsAnalytics.eventClickCategoryTabCategoryPage(categoryName)
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
        fun getCallingIntent(
            context: Context,
            categoryId: String? = null,
            isLandmarkPage: Boolean = false
        ): Intent = Intent(context, DealsCategoryActivity::class.java).apply {
            putExtra(EXTRA_CATEGORY_ID, categoryId)
            putExtra(IS_LANDMARK_PAGE, isLandmarkPage)
        }
    }
}
