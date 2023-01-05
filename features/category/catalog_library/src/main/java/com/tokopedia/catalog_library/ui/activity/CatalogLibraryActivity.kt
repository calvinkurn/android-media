package com.tokopedia.catalog_library.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.ui.fragment.CatalogHomepageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogLandingPageFragment

class CatalogLibraryActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_catalog_library)
        prepareView(savedInstanceState == null)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_catalog_library
    }

    private fun prepareView(savedInstanceIsNull: Boolean) {
        if (savedInstanceIsNull) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.catalog_home_frag,
                    CatalogHomepageFragment(),
                    CatalogHomepageFragment.CATALOG_HOME_PAGE_FRAGMENT_TAG
                ).commit()
        }
    }
}
