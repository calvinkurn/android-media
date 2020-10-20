package com.tokopedia.sellerorder.list.presentation.activities

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.list.presentation.fragments.SomListFragment

class SomListActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return SomListFragment()
    }
}