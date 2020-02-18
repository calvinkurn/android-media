package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PreferenceListActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return PreferenceListFragment()
    }
}
