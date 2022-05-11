package com.tokopedia.logisticorder.view.pod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

/**
 * POD = Proof Of Delivery
 *  called this activity
 *  - Tracking
 *  - Buyer Detail Order
 *  - 
 */
class ProofOfDeliveryActivity : BaseSimpleActivity() {


    companion object {

        const val POD_EXTRA_IMAGE_ID = "extra_image_id"
        const val POD_EXTRA_DESCRIPTION = "extra_description"

        const val REQUEST_POD = 22222

        const val RESULT_OK = 1
        const val RESULT_FAIL_LOAD_IMAGE = 2

    }

    override fun getNewFragment(): Fragment? {
        var fragment: ProofOfDeliveryFragment? = null
        if (intent.data?.lastPathSegment != null) {
            var orderId = intent.data?.lastPathSegment?.toLong()
            var imageId = intent.getStringExtra(POD_EXTRA_IMAGE_ID)
            var description = intent.getStringExtra(POD_EXTRA_DESCRIPTION)
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

