package com.tokopedia.logisticseller.ui.requestpickup.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_ID
import com.tokopedia.logisticseller.ui.requestpickup.presentation.fragment.RequestPickupFragment

/**
 * Created by fwidjaja on 2019-11-12.
 */
class RequestPickupActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
        }
        return RequestPickupFragment.newInstance(bundle)
    }
}
