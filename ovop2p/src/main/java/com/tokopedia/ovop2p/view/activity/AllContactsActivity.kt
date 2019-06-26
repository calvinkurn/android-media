package com.tokopedia.ovop2p.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.ovop2p.view.fragment.AllContactsFragment

class AllContactsActivity : BaseSimpleActivity(){
    override fun getNewFragment(): Fragment {
        return AllContactsFragment()
    }
}
