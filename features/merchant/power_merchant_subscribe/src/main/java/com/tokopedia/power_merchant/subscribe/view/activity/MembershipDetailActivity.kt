package com.tokopedia.power_merchant.subscribe.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.MembershipDetailFragment

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class MembershipDetailActivity : BaseSimpleActivity(),
    HasComponent<PowerMerchantSubscribeComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
    }

    override fun getNewFragment(): Fragment {
        return MembershipDetailFragment.createInstance()
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }

    private fun initInjector() {
        component.inject(this)
    }
}