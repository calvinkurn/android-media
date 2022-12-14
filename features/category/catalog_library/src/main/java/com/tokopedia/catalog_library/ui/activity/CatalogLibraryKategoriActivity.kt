package com.tokopedia.catalog_library.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.ui.fragment.CatalogLihatSemuaPageFragment

class CatalogLibraryKategoriActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return CatalogLihatSemuaPageFragment()
    }

    override fun getParentViewResourceID(): Int = R.id.catalog_home_frag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_library)
    }
}
