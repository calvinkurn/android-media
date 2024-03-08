package com.tokopedia.deals.ui.brand_detail

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.R
import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.di.DealsComponentFactory

class DealsBrandDetailActivity : BaseSimpleActivity(), HasComponent<DealsComponent> {

    private var seoUrl = ""

    override fun getNewFragment(): Fragment = DealsBrandDetailFragment.getInstance(seoUrl)

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            seoUrl = uri.query ?: ""
        } else if (savedInstanceState != null) {
            seoUrl = savedInstanceState.getString(EXTRA_SEO_URL, "")
        }

        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
    }

    override fun getComponent(): DealsComponent =
        DealsComponentFactory.instance.getDealsComponent(application, this)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_deals_share, menu)
        return true
    }

    companion object {
        const val EXTRA_SEO_URL = "EXTRA_SEO_URL"
    }
}
