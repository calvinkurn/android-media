package com.tokopedia.catalog_library.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.ui.fragment.CatalogHomepageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogLandingPageFragment

class CatalogLibraryKategoriCategoryActivity : BaseSimpleActivity() {

    private var categoryName = ""

    override fun getNewFragment(): Fragment {
        extractParameters()
        return CatalogLandingPageFragment.newInstance(categoryName)
    }

    override fun getParentViewResourceID(): Int = R.id.catalog_home_frag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_library)
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
        categoryName = if (pathSegments.size > 1) pathSegments[1] ?: "" else  ""
    }
}
