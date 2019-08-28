package com.tokopedia.discovery.catalogrevamp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class CatalogDetailPageActivity : BaseSimpleActivity() {

    companion object {
        private const val EXTRA_CATALOG_ID = "EXTRA_CATALOG_ID"

        @JvmStatic
        fun createIntent(context: Context, catalogId: String?): Intent {
            val intent = Intent(context, CatalogDetailPageActivity::class.java)
            intent.putExtra(EXTRA_CATALOG_ID, catalogId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment? {
        val catalogId: String = intent.getStringExtra(EXTRA_CATALOG_ID)
        return CatalogDetailPageFragment.newInstance(catalogId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(com.tokopedia.discovery.R.menu.menu_catalog_detail, menu)
        return super.onCreateOptionsMenu(menu)
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