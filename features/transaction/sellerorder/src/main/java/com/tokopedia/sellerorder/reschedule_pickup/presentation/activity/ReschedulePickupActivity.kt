package com.tokopedia.sellerorder.reschedule_pickup.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.reschedule_pickup.presentation.fragment.ReschedulePickupFragment

class ReschedulePickupActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(SomConsts.PARAM_ORDER_ID, "")
            bundle.putString(SomConsts.PARAM_COURIER_NAME, "")
        }
        return ReschedulePickupFragment.newInstance(bundle)
    }
}