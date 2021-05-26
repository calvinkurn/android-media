package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PinpointNewPageActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return PinpointNewPageFragment()
    }

}