package com.tokopedia.buy_more_get_more.olp.presentation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.buy_more_get_more.R

class OfferLandingPageActivity : BaseSimpleActivity() {

    private fun getShopIdFromUri(): String {
        val data = RouteManager.getIntent(this, intent.data.toString()).data
        return data?.pathSegments?.getOrNull(1) ?: ""
    }

    override fun getNewFragment(): Fragment =
        OfferLandingPageFragment.newInstance(getShopIdFromUri())

    override fun getLayoutRes() = R.layout.activity_offer_landing_page

    override fun getParentViewResourceID() = R.id.container


}
