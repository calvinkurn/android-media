package com.tokopedia.recharge_credit_card

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class RechargeCCActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return RechargeCCFragment.newInstance()
    }
}