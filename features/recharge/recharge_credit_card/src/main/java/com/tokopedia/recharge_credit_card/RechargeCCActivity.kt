package com.tokopedia.recharge_credit_card

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class RechargeCCActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return RechargeCCFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}