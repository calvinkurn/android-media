package com.tokopedia.logisticorder.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TrackingPageActivity : BaseSimpleActivity() {

    val ORDER_ID_KEY = "order_id"
    val URL_LIVE_TRACKING = "url_live_tracking"
    val ORDER_CALLER = "caller"


    override fun getNewFragment(): Fragment? {
        var fragment : TrackingPageFragment? = null
        if (intent.extras != null && intent.data?.lastPathSegment != null) {
            var orderId = intent?.data?.lastPathSegment
            var urlLiveTracking = intent?.data?.getQueryParameter(URL_LIVE_TRACKING)
            var orderCaller = intent?.data?.getQueryParameter(ORDER_CALLER)
            fragment = TrackingPageFragment.createFragment(orderId, urlLiveTracking, orderCaller)
        }
        return fragment
    }


}