package com.tokopedia.topads.auto.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topads.auto.view.fragment.InsufficientBalanceFragment

/**
 * Author errysuprayogi on 09,May,2019
 */
class InsufficientBalanceActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return InsufficientBalanceFragment.newInstance()
    }

}
