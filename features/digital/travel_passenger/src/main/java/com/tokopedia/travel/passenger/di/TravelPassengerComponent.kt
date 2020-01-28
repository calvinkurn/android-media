package com.tokopedia.travel.passenger.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.travel.passenger.presentation.fragment.TravelContactDataFragment
import dagger.Component

/**
 * @author by furqan on 03/01/2020
 */
@TravelPassengerScope
@Component(modules = [TravelPassengerModule::class, TravelPassengerViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TravelPassengerComponent {

    fun inject(travelContactDataFragment: TravelContactDataFragment)

}