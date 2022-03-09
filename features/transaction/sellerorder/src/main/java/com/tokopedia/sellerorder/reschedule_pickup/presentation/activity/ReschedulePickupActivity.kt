package com.tokopedia.sellerorder.reschedule_pickup.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.reschedule_pickup.presentation.fragment.ReschedulePickupFragment

class ReschedulePickupActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        var fragment: ReschedulePickupFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = ReschedulePickupFragment.newInstance(bundle ?: Bundle())
        }
        return fragment
    }
}