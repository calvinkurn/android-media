package com.tokopedia.digital_product_detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPDataPlanFragment
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPTagihanFragment
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPTokenListrikFragment
import com.tokopedia.digital_product_detail.presentation.monitoring.activity.DigitalPDPDataPlanTestSetupActivity
import com.tokopedia.digital_product_detail.presentation.monitoring.activity.DigitalPDPPulsaTestSetupActivity
import dagger.Component

/**
 * @author by firmanda on 04/01/21
 */

@DigitalPDPScope
@Component(modules = [DigitalPDPModule::class, DigitalPDPViewModelModule::class, DigitalPDPBindModule::class], dependencies = [BaseAppComponent::class])
interface DigitalPDPComponent {

    fun inject(digitalPDPPulsaFragment: DigitalPDPPulsaFragment)
    fun inject(digitalPDPDataPlanFragment: DigitalPDPDataPlanFragment)
    fun inject(digitalPDPTokenListrikFragment: DigitalPDPTokenListrikFragment)
    fun inject(digitalPDPTagihanFragment: DigitalPDPTagihanFragment)
    fun inject(digitalPDPPulsaActivity: DigitalPDPPulsaActivity)

    /** Macrobenchmark */
    fun inject(digitalPDPPulsaTestSetupActivity: DigitalPDPPulsaTestSetupActivity)
    fun inject(digitalPDPDataPlanTestSetupActivity: DigitalPDPDataPlanTestSetupActivity)
}
