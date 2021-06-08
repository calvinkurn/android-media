package com.tokopedia.power_merchant.subscribe.view_old.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.view_old.fragment.PowerMerchantSubscribeFragment

/**
 * Deeplink POWER_MERCHANT_SUBSCRIBE
 */
class PowerMerchantSubscribeActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, PowerMerchantSubscribeActivity::class.java)
        }
    }

    override fun getNewFragment(): Fragment {
        return PowerMerchantSubscribeFragment.createInstance()
    }

}
