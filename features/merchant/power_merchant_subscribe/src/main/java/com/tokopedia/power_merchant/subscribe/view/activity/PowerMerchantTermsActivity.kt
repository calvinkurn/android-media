package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantTermsFragment

/**
 * @author by milhamj on 14/06/19.
 */
class PowerMerchantTermsActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return PowerMerchantTermsFragment()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, PowerMerchantTermsActivity::class.java)
        }
    }
}