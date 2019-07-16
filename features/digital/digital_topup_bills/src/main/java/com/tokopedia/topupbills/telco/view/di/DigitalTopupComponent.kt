package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topupbills.telco.view.fragment.*
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = arrayOf(DigitalTopupModule::class, DigitalTopupViewModelModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface DigitalTopupComponent {

    fun inject(digitalTelcoFragment: DigitalTelcoFragment)

    fun inject(digitalTelcoPostpaidFragment: DigitalTelcoPostpaidFragment)

    fun inject(digitalTelcoPrepaidFragment: DigitalTelcoProductFragment)

    fun inject(digitalBaseTelcoFragment: DigitalBaseTelcoFragment)

    fun inject(digitalSearchNumberFragment: DigitalSearchNumberFragment)

}