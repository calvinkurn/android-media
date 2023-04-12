package com.tokopedia.catalog_library.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.ui.fragment.CatalogBrandLandingPageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogPopularBrandsFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogLibraryPopularBrandsActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        toolbar?.hide()
        return CatalogPopularBrandsFragment.newInstance()
    }
}
