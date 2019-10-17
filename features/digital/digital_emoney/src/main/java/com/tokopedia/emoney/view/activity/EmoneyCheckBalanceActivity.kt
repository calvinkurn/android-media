package com.tokopedia.emoney.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceNFCFragment

class EmoneyCheckBalanceActivity: BaseSimpleActivity() {

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getNewFragment(): Fragment? {
        return EmoneyCheckBalanceNFCFragment.newInstance()
    }
}