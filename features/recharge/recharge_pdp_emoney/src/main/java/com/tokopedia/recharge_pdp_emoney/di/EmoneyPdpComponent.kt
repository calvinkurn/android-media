package com.tokopedia.recharge_pdp_emoney.di

import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import dagger.Component

/**
 * @author by jessica on 29/03/21
 */

@EmoneyPdpScope
@Component(modules = [EmoneyPdpModule::class, EmoneyPdpViewModelModule::class],
        dependencies = [DigitalCommonComponent::class])
interface EmoneyPdpComponent {
    fun inject(emoneyPdpFragment: EmoneyPdpFragment)
}