package com.tokopedia.logisticseller.reschedulepickup.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.reschedulepickup.presentation.fragment.ReschedulePickupFragment

class ReschedulePickupActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            val orderId = intent?.data?.getQueryParameter(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
            bundle.putString(LogisticSellerConst.PARAM_ORDER_ID, orderId)
        }
        return ReschedulePickupFragment.newInstance(bundle)
    }
}