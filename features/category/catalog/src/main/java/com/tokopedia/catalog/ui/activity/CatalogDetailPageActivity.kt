package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.oldcatalog.ui.activity.CatalogDetailPageActivity as OldCatalogDetailPageActivity
import com.tokopedia.core.analytics.AppScreen

class CatalogDetailPageActivity :  BaseSimpleActivity() {

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

    override fun getScreenName(): String? {
        return "${AppScreen.SCREEN_CATALOG} - $catalogId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_detail_page)
        catalogId = if (intent.hasExtra(EXTRA_CATALOG_ID))
            intent.getStringExtra(EXTRA_CATALOG_ID) ?: ""
        else {
            val pathSegments = Uri.parse(intent.data?.path ?: "").pathSegments
            pathSegments.firstOrNull()?.split("-")?.lastOrNull()?.trim() ?: ""
        }
        catalogId = catalogId.split("-").lastOrNull()?.trim() ?: ""

        handleRollenceRoute(savedInstanceState, catalogId)
    }

    private fun handleRollenceRoute(savedInstanceState: Bundle?, catalogId: String) {
        val rollenceEnabled = true // TODO: implement rollence
        if (rollenceEnabled) {
            prepareView(savedInstanceState == null)
        } else {
            val intent = OldCatalogDetailPageActivity.createIntent(this, catalogId)
            startActivity(intent)
            finish()
        }
    }

    private fun prepareView(savedInstanceIsNull: Boolean) {
        if(savedInstanceIsNull) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.catalog_detail_parent_view,
                            CatalogDetailPageFragment.newInstance(catalogId),
                            CatalogDetailPageFragment.CATALOG_DETAIL_PAGE_FRAGMENT_TAG
                    )
                    .commit()
        }
    }
}
