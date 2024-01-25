package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogSwitchingComparisonFragment
import com.tokopedia.catalog.ui.fragment.CatalogSwitchingComparisonFragment.Companion.CATALOG_CHANGE_COMPARISON_TAG

class CatalogSwitchingComparisonActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_switching_comparison)
        val catalogId =
            intent.getStringExtra(CatalogSwitchingComparisonFragment.ARG_CATALOG_ID).orEmpty()
        val compareCatalogId =
            intent.getStringArrayListExtra(CatalogSwitchingComparisonFragment.ARG_COMPARISON_CATALOG_ID)
                .orEmpty()
        val categoryId =
            intent.getStringExtra(CatalogSwitchingComparisonFragment.ARG_EXTRA_CATALOG_CATEGORY_ID)
                .orEmpty()
        val brand =
            intent.getStringExtra(CatalogSwitchingComparisonFragment.ARG_EXTRA_CATALOG_BRAND).orEmpty()

        val fragment = CatalogSwitchingComparisonFragment.newInstance(
            catalogId,
            compareCatalogId,
            categoryId,
            brand
        )
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.catalog_change_comparison_parent_view,
                fragment,
                CATALOG_CHANGE_COMPARISON_TAG
            )
            .commit()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)
    }
}
