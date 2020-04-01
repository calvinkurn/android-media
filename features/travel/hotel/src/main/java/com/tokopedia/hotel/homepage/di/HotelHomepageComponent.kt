package com.tokopedia.hotel.homepage.di

import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.homepage.presentation.fragment.HotelHomepageFragment
import dagger.Component

/**
 * @author by furqan on 28/03/19
 */
@HotelHomepageScope
@Component(modules = [HotelHomepageModule::class, HotelHomepageViewModelModule::class], dependencies = [HotelComponent::class])
interface HotelHomepageComponent {

    fun inject(hotelHomepageFragment: HotelHomepageFragment)

}