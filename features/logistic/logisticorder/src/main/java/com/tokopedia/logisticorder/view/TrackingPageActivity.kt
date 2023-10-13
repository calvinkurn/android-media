package com.tokopedia.logisticorder.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TrackingPageActivity : BaseSimpleActivity() {

    companion object {
        private const val URL_LIVE_TRACKING = "url_live_tracking"
        private const val ORDER_CALLER = "caller"
        private const val TX_ID = "tx_id"
        private const val GROUP_TYPE = "group_type"
    }

    override fun getLayoutRes() = com.tokopedia.logisticorder.R.layout.activity_tracking_page
    override fun getParentViewResourceID() = com.tokopedia.logisticorder.R.id.tracking_page_container

    override fun getNewFragment(): Fragment? {
        var fragment: TrackingPageFragment? = null
        if (intent.data?.lastPathSegment != null) {
            val orderId = intent?.data?.lastPathSegment
            val urlLiveTracking = intent?.data?.getQueryParameter(URL_LIVE_TRACKING)
            val orderCaller = intent?.data?.getQueryParameter(ORDER_CALLER)
            val orderTxId = intent?.data?.getQueryParameter(TX_ID)
            val groupType = intent?.data?.getQueryParameter(GROUP_TYPE)?.toIntOrNull()
            fragment = TrackingPageFragment.createFragment(
                orderId,
                orderTxId,
                groupType,
                urlLiveTracking,
                orderCaller
            )
        }
        return fragment
    }
}
