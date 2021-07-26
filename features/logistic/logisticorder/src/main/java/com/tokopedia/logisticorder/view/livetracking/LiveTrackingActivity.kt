package com.tokopedia.logisticorder.view.livetracking

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticorder.view.livetracking.LiveTrackingFragment.Companion.EXTRA_TRACKING_URL

class LiveTrackingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val trackingUrl = intent.getStringExtra(EXTRA_TRACKING_URL)
        if (trackingUrl == null) {
            finish()
            return null
        }
        return LiveTrackingFragment.createInstance(trackingUrl)
    }

    companion object {

        fun createIntent(context: Context, trackingUrl: String?): Intent {
            return Intent(context, LiveTrackingActivity::class.java).putExtra(EXTRA_TRACKING_URL, trackingUrl)
        }
    }

}