package com.tokopedia.topupbills.di

import com.tokopedia.topupbills.common.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoScope
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoViewModelModule
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTelcoScope
@Component(modules = [DigitalTelcoModuleStub::class, DigitalTelcoViewModelModule::class], dependencies = [DigitalTopupComponent::class])
interface DigitalTelcoComponentStub: DigitalTelcoComponent {

}
