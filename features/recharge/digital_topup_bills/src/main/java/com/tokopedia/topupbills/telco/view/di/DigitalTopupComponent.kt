package com.tokopedia.topupbills.telco.view.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.topupbills.telco.view.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoProductFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = [DigitalTopupModule::class, DigitalTopupViewModelModule::class], dependencies = [CommonTopupBillsComponent::class])
interface DigitalTopupComponent {

    fun inject(digitalTelcoFragment: DigitalTelcoFragment)

    fun inject(digitalBaseTelcoFragment: DigitalBaseTelcoFragment)

    fun inject(digitalTelcoProductFragment: DigitalTelcoProductFragment)

    fun inject(digitalSearchNumberFragment: DigitalSearchNumberFragment)

}