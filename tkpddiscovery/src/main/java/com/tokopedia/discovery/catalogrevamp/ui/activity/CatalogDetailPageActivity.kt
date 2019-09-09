package com.tokopedia.discovery.catalogrevamp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.core.share.DefaultShare
import com.tokopedia.core2.R
import com.tokopedia.discovery.catalogrevamp.analytics.CatalogDetailPageAnalytics
import com.tokopedia.discovery.catalogrevamp.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.linker.model.LinkerData

class CatalogDetailPageActivity : BaseSimpleActivity(), CatalogDetailPageFragment.Listener {
    private var shareData: LinkerData? = null

    object DeeplinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.DISCOVERY_CATALOG)
        fun defaultIntent(context: Context, bundle: Bundle): Intent {
            val intent = createIntent(context, bundle.getString(EXTRA_CATALOG_ID),
                    bundle.getString(EXTRA_CATEGORY_DEPARTMENT_ID),
                    bundle.getString(EXTRA_CATEGORY_DEPARTMENT_NAME))
            return intent
                    .putExtras(bundle)
        }
    }

    companion object {
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_ID = "CATEGORY_ID"
        private const val EXTRA_CATEGORY_DEPARTMENT_NAME = "CATEGORY_NAME"
        @JvmStatic
        fun createIntent(context: Context, catalogId: String?, departmentId: String?, departmentName: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_ID, departmentId)
            intent.putExtra(EXTRA_CATEGORY_DEPARTMENT_NAME, departmentName)
            return intent
        }
    }

    override fun getScreenName(): String? {
        return AppScreen.SCREEN_CATALOG
    }

    override fun getNewFragment(): Fragment? {
        val catalogId: String = intent.getStringExtra(EXTRA_CATALOG_ID)
        val departmentId: String = intent.getStringExtra(EXTRA_CATEGORY_DEPARTMENT_ID)
        val departmentName: String = intent.getStringExtra(EXTRA_CATEGORY_DEPARTMENT_NAME)
        val fragment = CatalogDetailPageFragment.newInstance(catalogId, departmentId, departmentName)
        fragment.setListener(this)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
            toolbar.setBackgroundResource(R.color.white)
        } else {
            toolbar.setBackgroundResource(R.drawable.bg_white_toolbar_drop_shadow)
        }
        if (supportActionBar != null)
            supportActionBar!!.setHomeAsUpIndicator(
                    R.drawable.ic_webview_back_button
            )
    }

    override fun deliverCatalogShareData(shareData: LinkerData, catalogHeading: String) {
        this.shareData = shareData
        updateTitle(catalogHeading)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(com.tokopedia.discovery.R.menu.menu_catalog_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.tokopedia.discovery.R.id.action_share_prod) {
            if (shareData != null) {
                CatalogDetailPageAnalytics.trackEventClickSocialShare()
                DefaultShare(this, shareData).show()
            } else
                NetworkErrorHelper.showSnackbar(this, "Data katalog belum tersedia")
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        checkFragmentisCatalogDetailPageFragment()?.let {
            it.onBackPress()
            return
        }
        super.onBackPressed()
    }


    private fun checkFragmentisCatalogDetailPageFragment(): CatalogDetailPageFragment? {
        val currentFragment = fragment
        return if (currentFragment is CatalogDetailPageFragment) {
            currentFragment
        } else {
            null
        }
    }
}