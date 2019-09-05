package com.tokopedia.discovery.catalogrevamp.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.core.analytics.AppScreen
import com.tokopedia.core.network.NetworkErrorHelper
import com.tokopedia.core.share.DefaultShare
import com.tokopedia.core2.R
import com.tokopedia.discovery.catalogrevamp.analytics.CatalogDetailPageAnalytics
import com.tokopedia.linker.model.LinkerData

class CatalogDetailPageActivity : BaseSimpleActivity(), CatalogDetailPageFragment.Listener {
    private var shareData: LinkerData? = null

    companion object {
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"

        @JvmStatic
        fun createIntent(context: Context, catalogId: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            return intent
        }
    }

    override fun getScreenName(): String? {
        return AppScreen.SCREEN_CATALOG
    }

    override fun getNewFragment(): Fragment? {
        val catalogId: String = intent.getStringExtra(EXTRA_CATALOG_ID)
        val fragment = CatalogDetailPageFragment.newInstance(catalogId)
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