package com.tokopedia.home.beranda.presentation.view.listener

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment

class DummyActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return HomeRevampFragment.newInstance(false)
    }
}
