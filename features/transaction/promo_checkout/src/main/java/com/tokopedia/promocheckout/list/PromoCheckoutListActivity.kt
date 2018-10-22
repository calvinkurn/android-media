package com.tokopedia.promocheckout.list

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PromoCheckoutListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutListFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context): Intent{
            return Intent(context, PromoCheckoutListActivity::class.java)
        }
    }
}