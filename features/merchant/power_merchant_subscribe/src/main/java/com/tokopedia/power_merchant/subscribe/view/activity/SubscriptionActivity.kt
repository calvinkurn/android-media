package com.tokopedia.power_merchant.subscribe.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment

/**
 * Created By @ilhamsuaib on 25/02/21
 */

class SubscriptionActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PowerMerchantSubscribeFragment.createInstance()
    }
}