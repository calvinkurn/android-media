package com.tokopedia.deals.ui.pdp.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.R
import com.tokopedia.deals.databinding.ActivityBaseDealsDetailBinding
import com.tokopedia.deals.di.DealsComponentFactory
import com.tokopedia.deals.ui.pdp.di.DaggerDealsPDPComponent
import com.tokopedia.deals.ui.pdp.di.DealsPDPComponent
import com.tokopedia.deals.ui.pdp.ui.callback.DealsPDPCallbacks
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPAllLocationFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPDescFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPFragment
import com.tokopedia.deals.ui.pdp.ui.fragment.DealsPDPSelectDealsQuantityFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.deals.R.anim as animDeals
import com.tokopedia.deals.R.id as idDeals
import com.tokopedia.deals.R.layout as layoutDeals

class DealsPDPActivity : BaseSimpleActivity(), HasComponent<DealsPDPComponent>, DealsPDPCallbacks {

    private var productId: String? = null
    private var binding: ActivityBaseDealsDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            productId = uri.query ?: ""
        } else if (savedInstanceState != null) {
            productId = savedInstanceState.getString(EXTRA_PRODUCT_ID, "")
        } else if (intent.extras != null) {
            productId = intent.getStringExtra(EXTRA_PRODUCT_ID)
        }
        super.onCreate(savedInstanceState)
        binding = ActivityBaseDealsDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        return DealsPDPFragment.createInstance(productId)
    }

    override fun getComponent(): DealsPDPComponent {
        return DaggerDealsPDPComponent.builder()
            .dealsComponent(DealsComponentFactory.instance.getDealsComponent(application, this))
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_deals_pdp, menu)
        return true
    }

    override fun getLayoutRes(): Int {
        return layoutDeals.activity_base_deals_detail
    }

    override fun getToolbarResourceID(): Int {
        return idDeals.toolbar_base_deals_detail
    }

    override fun getParentViewResourceID(): Int {
        return idDeals.deals_detail_parent_view
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onShowMoreDesc(title: String, text: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            animDeals.deals_slide_in_up,
            animDeals.deals_slide_in_down,
            animDeals.deals_slide_out_down,
            animDeals.deals_slide_out_up
        )
        transaction.add(idDeals.deals_detail_parent_view, DealsPDPDescFragment.createInstance(title, text))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onShowAllLocation(outlets: List<com.tokopedia.deals.ui.pdp.data.Outlet>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            animDeals.deals_slide_in_up,
            animDeals.deals_slide_in_down,
            animDeals.deals_slide_out_down,
            animDeals.deals_slide_out_up
        )
        transaction.add(idDeals.deals_detail_parent_view, DealsPDPAllLocationFragment.createInstance(outlets))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSelectQuantityProduct(data: com.tokopedia.deals.ui.pdp.data.ProductDetailData) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            animDeals.deals_slide_in_up,
            animDeals.deals_slide_in_down,
            animDeals.deals_slide_out_down,
            animDeals.deals_slide_out_up
        )
        transaction.add(idDeals.deals_detail_parent_view, DealsPDPSelectDealsQuantityFragment.createInstance(data))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onShowShareLoader() {
        binding?.dealsDetailShare?.show()
    }

    override fun onHideShareLoader() {
        binding?.dealsDetailShare?.hide()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
    }
}
