package com.tokopedia.logisticorder.view.pod.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber

/**
 * POD = Proof Of Delivery
 */
class ProofOfDeliveryActivity : BaseSimpleActivity() {

    companion object {

        const val EXTRA_IMAGE_ID = "extra_image_id"
        const val EXTRA_DESCRIPTION = "extra_description"

        const val REQUEST_POD = 22222

        const val RESULT_OK = 1
        const val RESULT_FAIL_LOAD_IMAGE = 2

    }

    override fun getNewFragment(): Fragment? {
        var fragment: ProofOfDeliveryFragment? = null
        if (intent.data?.lastPathSegment != null) {
            var orderId = intent.data?.lastPathSegment?.toLong()
            var imageId = intent.getStringExtra(EXTRA_IMAGE_ID)
            var description = intent.getStringExtra(EXTRA_DESCRIPTION)
            fragment = ProofOfDeliveryFragment.createFragment(orderId, imageId, description)
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

    }



}