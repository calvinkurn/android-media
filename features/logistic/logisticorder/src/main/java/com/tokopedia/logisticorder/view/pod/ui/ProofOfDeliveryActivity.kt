package com.tokopedia.logisticorder.view.pod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.logisticCommon.data.constant.PodConstant
import timber.log.Timber

/**
 * Created by irpan on 28/04/22.
 *
 * POD = Proof Of Delivery
 *  applink tokopedia://shipping/pod/{order_id}?image_id={image_id}&description={description}
 *  and
 *  ApplinkConstInternalLogistic.PROOF_OF_DELIVERY
 *
 */

class ProofOfDeliveryActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var fragment: ProofOfDeliveryFragment? = null
        if (intent.data?.lastPathSegment != null) {
            val orderId = intent.data?.lastPathSegment?.toLong()
            val imageId = intent.getStringExtra(PodConstant.QUERY_IMAGE_ID)
            val description = intent.getStringExtra(PodConstant.QUERY_DESCRIPTION)
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

