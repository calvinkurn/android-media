package com.tokopedia.catalog_library.ui.activity

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.ui.fragment.CatalogBrandLandingPageFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogLibraryBrandLandingActivity : BaseSimpleActivity() {

    private var brandId = ""
    private var brandName = ""

    override fun getNewFragment(): Fragment {
        toolbar?.hide()
        extractParameters()
        return CatalogBrandLandingPageFragment.newInstance(brandId, brandName)
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
        brandId = if (pathSegments.size > 1) pathSegments[1] ?: "" else ""
        brandName = if (pathSegments.size > 2) pathSegments[2] ?: "" else ""
    }
}
