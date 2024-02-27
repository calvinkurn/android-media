package com.tokopedia.catalog.ui.activity.sellerOfferingList

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogProductListFragment
import com.tokopedia.catalog.ui.fragment.CatalogSellerOfferingFragment

class CatalogSellerOfferingActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_product_list)
        setStatusBarToTransparent(this)
        prepareView()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun prepareView() {
        val fragment = CatalogSellerOfferingFragment()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.catalog_product_list_parent_view,
                fragment,
                CatalogProductListFragment.CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG
            )
            .commit()
    }

    private fun setStatusBarToTransparent(activity: Activity) {
        val window = activity.window
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
    }
}
