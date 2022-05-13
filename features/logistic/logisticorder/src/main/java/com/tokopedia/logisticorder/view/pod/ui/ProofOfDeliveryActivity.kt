package com.tokopedia.logisticorder.view.pod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import timber.log.Timber

/**
 * POD = Proof Of Delivery
 *  called this activity
 *  - Tracking
 *  - Buyer Detail Order
 *  - 
 */
class ProofOfDeliveryActivity : BaseSimpleActivity() {


    companion object {



        const val REQUEST_POD = 22222

        const val RESULT_OK = 1
        const val RESULT_FAIL_LOAD_IMAGE = 2

    }

    override fun getNewFragment(): Fragment? {
        var fragment: ProofOfDeliveryFragment? = null
        if (intent.data?.lastPathSegment != null) {
            var orderId = intent.data?.lastPathSegment?.toLong()
            var imageId = intent.getStringExtra(ApplinkConst.ProofOfDelivery.QUERY_IMAGE_ID)
            var description = intent.getStringExtra(ApplinkConst.ProofOfDelivery.QUERY_DESCRIPTION)
            fragment = ProofOfDeliveryFragment.createFragment(orderId, imageId, description)
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initHideActionBar()
    }

    private fun initHideActionBar() {
        supportActionBar?.hide()
    }

}

