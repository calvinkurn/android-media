package com.tokopedia.promocheckout.list

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PromoCheckoutListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        PromoCheckoutListFragment
    }
}