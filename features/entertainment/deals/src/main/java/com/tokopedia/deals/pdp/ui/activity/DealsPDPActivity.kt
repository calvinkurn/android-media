package com.tokopedia.deals.pdp.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.deals.DealsComponentInstance
import com.tokopedia.deals.R
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DaggerDealsPDPComponent
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.callback.DealsPDPCallbacks
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPAllLocationFragment
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPDescFragment
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPFragment
import com.tokopedia.deals.pdp.ui.fragment.DealsPDPSelectDealsQuantityFragment

class DealsPDPActivity : BaseSimpleActivity(), HasComponent<DealsPDPComponent>, DealsPDPCallbacks {

    var productId: String? = null

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
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        return DealsPDPFragment.createInstance(productId)
    }

    override fun getComponent(): DealsPDPComponent {
        return DaggerDealsPDPComponent.builder()
            .dealsComponent(DealsComponentInstance.getDealsComponent(application, this))
            .build()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_deals_pdp, menu)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun onShowMoreDesc(title: String, text: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            com.tokopedia.deals.R.anim.deals_slide_in_up,
            com.tokopedia.deals.R.anim.deals_slide_in_down,
            com.tokopedia.deals.R.anim.deals_slide_out_down,
            com.tokopedia.deals.R.anim.deals_slide_out_up
        )
        transaction.add(com.tokopedia.abstraction.R.id.parent_view, DealsPDPDescFragment.createInstance(title, text))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onShowAllLocation(outlets: List<Outlet>) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            com.tokopedia.deals.R.anim.deals_slide_in_up,
            com.tokopedia.deals.R.anim.deals_slide_in_down,
            com.tokopedia.deals.R.anim.deals_slide_out_down,
            com.tokopedia.deals.R.anim.deals_slide_out_up
        )
        transaction.add(com.tokopedia.abstraction.R.id.parent_view, DealsPDPAllLocationFragment.createInstance(outlets))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSelectQuantityProduct(data: ProductDetailData) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            com.tokopedia.deals.R.anim.deals_slide_in_up,
            com.tokopedia.deals.R.anim.deals_slide_in_down,
            com.tokopedia.deals.R.anim.deals_slide_out_down,
            com.tokopedia.deals.R.anim.deals_slide_out_up
        )
        transaction.add(com.tokopedia.abstraction.R.id.parent_view, DealsPDPSelectDealsQuantityFragment.createInstance(data))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID"
    }
}
