package com.tokopedia.catalog.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATALOG_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_CATEGORY_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.ARG_PARAM_COMPARE_CATALOG_ID
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG

class CatalogComparisonDetailActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_comparison_detail)
        val catalogId = intent.getStringExtra(ARG_PARAM_CATALOG_ID).orEmpty()
        val categoryId = intent.getStringExtra(ARG_PARAM_CATEGORY_ID).orEmpty()
        val compareCatalogId = intent.getStringArrayListExtra(ARG_PARAM_COMPARE_CATALOG_ID).orEmpty()
        val fragment = CatalogComparisonDetailFragment.newInstance(catalogId, categoryId, compareCatalogId)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.catalog_comparison_detail_parent_view,
                fragment,
                CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG
            )
            .commit()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}
