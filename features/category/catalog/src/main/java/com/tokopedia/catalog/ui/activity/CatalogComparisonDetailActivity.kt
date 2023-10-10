package com.tokopedia.catalog.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment.Companion.CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG

class CatalogComparisonDetailActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_comparison_detail)
        supportFragmentManager.beginTransaction()
            .replace(R.id.catalog_comparison_detail_parent_view,
                CatalogComparisonDetailFragment.newInstance(),
                CATALOG_COMPARISON_DETAIL_FRAGMENT_TAG
            )
            .commit()
    }

    override fun getNewFragment(): Fragment? {
       return null
    }
}
