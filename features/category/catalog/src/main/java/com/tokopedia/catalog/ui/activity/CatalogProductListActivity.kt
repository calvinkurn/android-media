package com.tokopedia.catalog.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogProductListFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductListImprovementFragment
import com.tokopedia.catalog.ui.fragment.CatalogSellerOfferingFragment
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_IMPROVMENT_CATALOG_PRODUCT_LIST

class CatalogProductListActivity : BaseSimpleActivity() {
    private var catalogId: String = ""
    private var catalogTitle: String = ""
    private var productSortingStatus: String = ""
    private var catalogUrl: String = ""

    private var productTitle: String = ""
    private var productVariant: String = ""
    private var background: String = ""
    private var isSellerOffering = ""
    private var limit = "20"
    private var minPrice = "0"
    private var maxPrice = "0"

    var remoteConfig: FirebaseRemoteConfigImpl? = null

    companion object {
        private const val CATALOG_DETAIL_TAG = "CATALOG_DETAIL_TAG"
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
        const val EXTRA_CATALOG_URL = "EXTRA_CATALOG_URL"
        private const val QUERY_CATALOG_ID = "catalog_id"
        private const val QUERY_PRODUCT_SORTING_STATUS = "sorting_status"

        const val QUERY_PRODUCT_TITLE= "product_title"
        const val QUERY_PRODUCT_VARIANT= "product_variant"
        const val QUERY_BACKGROUND= "background"
        const val QUERY_LIMIT= "limit"
        const val QUERY_PRODUCT_ID= "product_id"
        const val QUERY_SHOP_ID= "shop_id"
        const val QUERY_MIN_PRICE= "min_price"
        const val QUERY_MAX_PRICE= "max_price"
        private const val FLAG_SELLER_OFFERING = "so"
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

        isSellerOffering = intent?.data?.lastPathSegment.toString()
        productTitle = intent.data?.getQueryParameter(QUERY_PRODUCT_TITLE).orEmpty()
        productVariant = intent.data?.getQueryParameter(QUERY_PRODUCT_VARIANT).orEmpty()
        background = intent.data?.getQueryParameter(QUERY_BACKGROUND).orEmpty()
        limit = intent.data?.getQueryParameter(QUERY_LIMIT).orEmpty()
        minPrice = intent.data?.getQueryParameter(QUERY_MIN_PRICE).orEmpty()
        maxPrice = intent.data?.getQueryParameter(QUERY_MAX_PRICE).orEmpty()

        prepareView(savedInstanceState == null)
    }

    private fun prepareView(savedInstanceIsNull: Boolean) {
        if (savedInstanceIsNull) {
            val fragment = isSellerOffering()
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.catalog_product_list_parent_view,
                    fragment,
                    CatalogProductListFragment.CATALOG_PRODUCT_LIST_PAGE_FRAGMENT_TAG
                )
                .commit()
        }
    }

    private fun isSellerOffering() : Fragment{
        return if (isSellerOffering == FLAG_SELLER_OFFERING){
            setStatusBarToTransparent(this)
            CatalogSellerOfferingFragment.newInstance(
                catalogId,
                productTitle,
                productVariant,
                background,
                limit,
                minPrice,
                maxPrice
            )
        }else{
            if (remoteConfig?.getBoolean(ENABLE_IMPROVMENT_CATALOG_PRODUCT_LIST).orFalse()) {
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
        }
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
