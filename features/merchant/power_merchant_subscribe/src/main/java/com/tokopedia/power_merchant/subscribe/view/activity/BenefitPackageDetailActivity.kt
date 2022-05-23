package com.tokopedia.power_merchant.subscribe.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.BenefitPackageDetailFragment

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class BenefitPackageDetailActivity : BaseSimpleActivity(), HasComponent<PowerMerchantSubscribeComponent> {

    override fun getNewFragment(): Fragment {
        return BenefitPackageDetailFragment.createInstance()
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        val appComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerPowerMerchantSubscribeComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }
}