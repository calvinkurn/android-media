package com.tokopedia.tokomart.home.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.tokomart.home.presentation.fragment.TokoMartHomeFragment

class TokoMartHomeActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = TokoMartHomeFragment.newInstance()
}