package com.tokopedia.power_merchant.subscribe.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.power_merchant.subscribe.di.DaggerPowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.fragment.BenefitPackageFragment

class BenefitPackageActivity : BaseSimpleActivity(), HasComponent<PowerMerchantSubscribeComponent> {

    override fun getNewFragment(): Fragment? {
        return BenefitPackageFragment.newInstance()
    }

    override fun getComponent(): PowerMerchantSubscribeComponent {
        return DaggerPowerMerchantSubscribeComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}