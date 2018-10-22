package com.tokopedia.promocheckout.detail

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class PromoCheckoutDetailActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PromoCheckoutDetailFragment.createInstance()
    }

    companion object {
        fun createIntent(activity: FragmentActivity?, id: Int?):Intent{

        }
    }
}