package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.campaign.helper.BuyMoreGetMoreHelper
import java.util.ArrayList

class OfferLandingPageActivity : BaseSimpleActivity() {

    private fun getShopIdFromUri(): String {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.getOrNull(1) ?: ""
    }

    private fun getOfferIdFromUri(): String {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.getOrNull(3) ?: ""
    }

    private fun getWarehouseIds(): ArrayList<Int>? {
        val bundle = intent.extras
        return bundle?.getIntegerArrayList(BuyMoreGetMoreHelper.KEY_WAREHOUSE_IDS)
    }

    private fun getProductIds(): ArrayList<Int>? {
        val bundle = intent.extras
        return bundle?.getIntegerArrayList(BuyMoreGetMoreHelper.KEY_PRODUCT_IDS)
    }

    override fun getNewFragment(): Fragment =
        OfferLandingPageFragment.newInstance(
            getShopIdFromUri(),
            getOfferIdFromUri(),
            getWarehouseIds(),
            getProductIds()
        )

    override fun getLayoutRes() = R.layout.activity_offer_landing_page

    override fun getParentViewResourceID() = R.id.container


}
