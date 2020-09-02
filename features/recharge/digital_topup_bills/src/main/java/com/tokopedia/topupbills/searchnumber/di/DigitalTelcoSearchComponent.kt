package com.tokopedia.topupbills.searchnumber.di

import com.tokopedia.topupbills.common.di.DigitalTopupComponent
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberFragment
import dagger.Component

/**
 * Created by nabillasabbaha on 07/05/19.
 */
@DigitalTelcoSearchScope
@Component(modules = [DigitalTelcoSearchModule::class], dependencies = [DigitalTopupComponent::class])
interface DigitalTelcoSearchComponent {

    fun inject(digitalSearchNumberFragment: DigitalSearchNumberFragment)

}