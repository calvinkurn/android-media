package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.topupbills.telco.view.fragment.*
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = arrayOf(DigitalTopupModule::class, DigitalTopupViewModelModule::class),
        dependencies = arrayOf(DigitalCommonComponent::class))
interface DigitalTopupComponent {

    fun inject(digitalTelcoFragment: DigitalTelcoFragment)

    fun inject(digitalTelcoPostpaidFragment: DigitalTelcoPostpaidFragment)

    fun inject(digitalTelcoPrepaidFragment: DigitalTelcoProductFragment)

    fun inject(digitalBaseTelcoFragment: DigitalBaseTelcoFragment)

    fun inject(digitalSearchNumberFragment: DigitalSearchNumberFragment)

}