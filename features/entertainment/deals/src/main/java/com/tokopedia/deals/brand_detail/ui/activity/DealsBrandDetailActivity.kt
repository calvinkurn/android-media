package com.tokopedia.deals.brand_detail.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.R
import com.tokopedia.deals.brand_detail.di.DaggerDealsBrandDetailComponent
import com.tokopedia.deals.brand_detail.di.DealsBrandDetailComponent
import com.tokopedia.deals.brand_detail.ui.fragment.DealsBrandDetailFragment

class DealsBrandDetailActivity: BaseSimpleActivity(), HasComponent<DealsBrandDetailComponent> {

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

    override fun getComponent(): DealsBrandDetailComponent =
            DaggerDealsBrandDetailComponent.builder()
                    .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
                    .build()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_deals_share, menu)
        return true
    }

    companion object {
        const val EXTRA_SEO_URL = "EXTRA_SEO_URL"
    }
}