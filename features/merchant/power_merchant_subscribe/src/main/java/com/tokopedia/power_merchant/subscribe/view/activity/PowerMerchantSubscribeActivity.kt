package com.tokopedia.power_merchant.subscribe.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment

class PowerMerchantSubscribeActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power_merchant_subscribe)
    }

    override fun getNewFragment(): Fragment {
        return PowerMerchantSubscribeFragment.createInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
