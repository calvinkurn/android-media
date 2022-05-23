package com.tokopedia.power_merchant.subscribe.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class BenefitPackageDetailFragment : BaseDaggerFragment() {

    companion object {
        fun createInstance(): BenefitPackageDetailFragment {
            return BenefitPackageDetailFragment()
        }
    }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }


}