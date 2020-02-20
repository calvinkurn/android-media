package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class AddressListActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment? {
        return AddressListFragment()
    }
}