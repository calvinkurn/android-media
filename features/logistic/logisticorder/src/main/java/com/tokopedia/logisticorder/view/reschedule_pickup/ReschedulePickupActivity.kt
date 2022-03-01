package com.tokopedia.logisticorder.view.reschedule_pickup

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ReschedulePickupActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return ReschedulePickupFragment.newInstance()
    }
}