package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoPrepaidFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoProductFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = arrayOf(DigitalTopupModule::class, DigitalTopupViewModelModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface DigitalTopupComponent {

    fun inject(digitalTelcoPrepaidFragment: DigitalTelcoPrepaidFragment)
    fun inject(digitalTelcoPrepaidFragment: DigitalTelcoProductFragment)

}