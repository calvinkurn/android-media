package com.tokopedia.logisticorder.view.reschedule_pickup

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ReschedulePickupActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        var fragment: ReschedulePickupFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = ReschedulePickupFragment.newInstance(bundle ?: Bundle())
        }
        return fragment
    }

    companion object {
        private const val EXTRA_COURIER_NAME = "courier_name"
        private const val EXTRA_INVOICE = "invoice"
    }
}