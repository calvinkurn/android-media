package com.tokopedia.broadcaster.chucker.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.broadcaster.chucker.ui.fragment.NetworkChuckerFragment

class NetworkChuckerActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return NetworkChuckerFragment()
    }

}