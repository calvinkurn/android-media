package com.tokopedia.catalog_library.ui.activity

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.ui.fragment.CatalogLandingPageFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogLibraryLandingPageActivity : BaseSimpleActivity() {

    private var categoryIdentifier = ""

    override fun getNewFragment(): Fragment {
        toolbar?.hide()
        extractParameters()
        return CatalogLandingPageFragment.newInstance(categoryIdentifier)
    }

    private fun extractParameters() {
        val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
        categoryIdentifier = if (pathSegments.size > 1) pathSegments[1] ?: "" else ""
    }
}
