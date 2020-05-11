package com.tokopedia.home

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener

class HomeActivity : BaseSimpleActivity(), MainParentStatusBarListener {
    override fun getNewFragment(): Fragment? {
        return HomeFragment()
    }

    override fun requestStatusBarLight() {

    }

    override fun requestStatusBarDark() {

    }
}