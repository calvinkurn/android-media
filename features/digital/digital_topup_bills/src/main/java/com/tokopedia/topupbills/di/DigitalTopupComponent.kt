package com.tokopedia.topupbills.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topupbills.fragment.DigitalTelcoPrepaidFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTopupScope
@Component(modules = arrayOf(DigitalTopupModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface DigitalTopupComponent {

    fun inject(digitalTelcoPrepaidFragment: DigitalTelcoPrepaidFragment)

}