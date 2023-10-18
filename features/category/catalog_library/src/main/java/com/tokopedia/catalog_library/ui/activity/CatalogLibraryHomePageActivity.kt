package com.tokopedia.catalog_library.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.ui.fragment.CatalogHomepageFragment
import com.tokopedia.kotlin.extensions.view.hide

class CatalogLibraryHomePageActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        toolbar?.hide()
        return CatalogHomepageFragment.getInstance()
    }
}
