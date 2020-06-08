package com.tokopedia.manageaddress.ui

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ManageAddressActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return ManageAddressFragment()
    }
}