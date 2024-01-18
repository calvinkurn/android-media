package com.tokopedia.catalog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogProductListFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductListImprovementFragment
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_IMPROVMENT_CATALOG_PRODUCT_LIST

class CatalogProductListActivity : BaseSimpleActivity() {
    private var catalogId: String = ""
    private var catalogTitle: String = ""
    private var productSortingStatus: String = ""
    private var catalogUrl: String = ""

    var remoteConfig: FirebaseRemoteConfigImpl? = null

    companion object {
        private const val CATALOG_DETAIL_TAG = "CATALOG_DETAIL_TAG"
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
        const val EXTRA_CATALOG_URL = "EXTRA_CATALOG_URL"
        private const val QUERY_CATALOG_ID = "catalog_id"
        private const val QUERY_PRODUCT_SORTING_STATUS = "sorting_status"

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
        setContentView(R.layout.activity_catalog_product_list)
        remoteConfig = FirebaseRemoteConfigImpl(this)
        catalogId = if (intent.hasExtra(EXTRA_CATALOG_ID)) {
            intent.getStringExtra(EXTRA_CATALOG_ID) ?: ""
        } else {
            intent.data?.getQueryParameter(QUERY_CATALOG_ID).orEmpty()
        }

        catalogUrl = intent.getStringExtra(EXTRA_CATALOG_URL) ?: ""

        catalogTitle = intent?.data?.lastPathSegment.toString()
        productSortingStatus =
            intent?.data?.getQueryParameter(QUERY_PRODUCT_SORTING_STATUS).toString()
        prepareView(savedInstanceState == null)
    }

    private fun prepareView(savedInstanceIsNull: Boolean) {
        if (savedInstanceIsNull) {
            val fragment = if (remoteConfig?.getBoolean(ENABLE_IMPROVMENT_CATALOG_PRODUCT_LIST).orFalse()) {
                CatalogProductListImprovementFragment.newInstance(
                    catalogId,
                    catalogTitle,
                    productSortingStatus,
                    catalogUrl
                )
            } else {
                CatalogProductListFragment.newInstance(
                    catalogId,
                    catalogTitle,
                    productSortingStatus,
                    catalogUrl
                )
            }
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.catalog_product_list_parent_view,
                    fragment,
                    CatalogProductListFragment.CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG
                )
                .commit()
        }
    }

}
