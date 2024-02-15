package com.tokopedia.catalog.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogLandingPageFragment
import com.tokopedia.oldcatalog.ui.activity.CatalogDetailPageActivity as OldCatalogDetailPageActivity

class CatalogDetailPageActivity :
    BaseSimpleActivity(),
    CatalogLandingPageFragment.CatalogLandingPageFragmentListener {

    private var catalogId: String = ""

    companion object {
        private const val CATALOG_DETAIL_TAG = "CATALOG_DETAIL_TAG"
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"

        @JvmStatic
        fun createIntent(context: Context, catalogId: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getTagFragment(): String {
        return CATALOG_DETAIL_TAG
    }

    override fun getScreenName() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_detail_page)
        setStatusBarToTransparent(this)
        catalogId = if (intent.hasExtra(EXTRA_CATALOG_ID)) {
            intent.getStringExtra(EXTRA_CATALOG_ID) ?: ""
        } else {
            val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
            pathSegments.firstOrNull()?.split("-")?.lastOrNull()?.trim() ?: ""
        }
        catalogId = catalogId.split("-").lastOrNull()?.trim() ?: ""

        handleVersionRoute(savedInstanceState)
    }

    override fun onLayoutBelowVersion3() {
        val intent = OldCatalogDetailPageActivity.createIntent(this, catalogId)
        startActivity(intent)
        finish()
    }

    override fun onLayoutAboveVersion4() {
        loadV4Layout()
    }

    private fun handleVersionRoute(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            prepareLoader()
        }
    }

    private fun loadV4Layout() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.catalog_detail_parent_view,
                CatalogDetailPageFragment.newInstance(catalogId),
                CatalogDetailPageFragment.CATALOG_DETAIL_PAGE_FRAGMENT_TAG
            )
            .commit()
    }

    private fun prepareLoader() {
        val fragment = CatalogLandingPageFragment.newInstance(catalogId)
        fragment.setListener(this)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.catalog_detail_parent_view,
                fragment,
                CatalogLandingPageFragment.CATALOG_LOADER_PAGE_FRAGMENT_TAG
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

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)
    }
}
